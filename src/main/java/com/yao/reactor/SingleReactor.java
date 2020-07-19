package com.yao.reactor;

import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Reactor 单线程模式
 *
 * @author qingju.yao
 * @date 2020/07/08
 */
public class SingleReactor {

    private Selector selector;
    private ServerSocketChannel channel;

    public void bind(int port) throws IOException {

        selector = Selector.open();
        channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(port));
        // 非阻塞模式
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(new Accept());

        while (!Thread.interrupted()) {
            selector.select();
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                dispatch(iterator.next());
            }
            selectionKeySet.clear();
        }
    }

    /**
     * 对应事件任务执行
     * - 服务器的任务接收
     * - 处理器的数据处理
     *
     * @param key
     * @author qingju.yao
     * @date 2020/7/8
     */
    private void dispatch(SelectionKey key) {

        Runnable runnable = (Runnable) key.attachment();
        if (null != runnable) {
            runnable.run();
        }
    }

    /**
     * 服务请求接收
     *
     * @author qingju.yao
     * @date 2020/7/8
     */
    public class Accept implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            SocketChannel socket = channel.accept();
            if (null != socket) {
                new Handler(selector, socket);
            }
        }
    }

    /**
     * 消息处理
     * - 绑定监听
     * - 监听场景处理（读、写）
     *
     * @author qingju.yao
     * @date 2020/7/8
     */
    public class Handler implements Runnable {

        private final Boolean READ = true;
        private final Boolean WRITE = false;

        private final SocketChannel socket;
        private final SelectionKey key;

        private String content = "\n\tResponse:\t";
        private boolean state = READ;

        public Handler(Selector selector, SocketChannel socket) throws IOException {

            this.socket = socket;
            // 非阻塞模式
            this.socket.configureBlocking(false);
            this.key = socket.register(selector, SelectionKey.OP_READ);
            key.attach(this);
            // 绑定后，快速恢复
            selector.wakeup();
        }

        @SneakyThrows
        @Override
        public void run() {

            if (state) {
                read();
                process();
            } else {
                send();
            }
        }

        /**
         * 阻塞至数据书写完毕
         *
         * @author qingju.yao
         * @date 2020/7/8
         */
        private void send() throws IOException {
            ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(CharsetUtil.UTF_8));
            socket.write(buffer);
            key.interestOps(SelectionKey.OP_READ);
            state = READ;
        }

        private void process() {
            System.out.println("Handler.receive:\t" + content);
            key.interestOps(SelectionKey.OP_WRITE);
            state = WRITE;
        }

        /**
         * 阻塞至数据读取完毕
         *
         * @author qingju.yao
         * @date 2020/7/8
         */
        public void read() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readableLength = 0;
            do {
                socket.read(buffer);
                buffer.flip();
                readableLength = buffer.remaining();
                if (readableLength <= 0) {
                    break;
                }

                byte[] bytes = new byte[readableLength];
                buffer.get(bytes);
                content += new String(bytes, CharsetUtil.UTF_8);
                buffer.clear();
            } while (true);
        }
    }
}
