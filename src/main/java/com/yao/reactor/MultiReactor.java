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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Reactor 多 Selector 模式
 *
 * @author qingju.yao
 * @date 2020/07/08
 */
public class MultiReactor {

    private Selector[] selectors;
    private ServerSocketChannel channel;

    public void bind(int port) throws IOException {

        selectors = new Selector[]{
                Selector.open(),
                Selector.open()
        };
        channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(port));
        // 非阻塞模式
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selectors[0], SelectionKey.OP_ACCEPT);
        key.attach(new Accept());

        new Thread(new SubReactor(selectors[1])).start();
        new SubReactor(selectors[0]).run();
    }

    /**
     * 模块监听
     *
     * @author qingju.yao
     * @date 2020/7/8
     */
    public class SubReactor implements Runnable {

        private final Selector selector;

        public SubReactor(Selector selector) {
            this.selector = selector;
        }

        @SneakyThrows
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                this.selector.select();
                Set<SelectionKey> selectionKeySet = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                }
                selectionKeySet.clear();
            }
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
                new Handler(selectors[1], socket);
                // 绑定后，快速恢复 - 此处要优先触发 Selector[0]
                selectors[0].wakeup();
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

        private static final int READING = 1 << 0;
        private static final int PROCESSING = 1 << 1;
        private static final int WRITING = 1 << 2;

        private final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
        private final Executor executor = new ThreadPoolExecutor(
                PROCESSOR_COUNT, 2 * PROCESSOR_COUNT,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10 * PROCESSOR_COUNT));

        private final Selector selector;
        private final SocketChannel socket;
        private final SelectionKey key;

        private String content = "\n\tResponse:\t";
        private int state = READING;
        private Object lock = new Object();

        public Handler(Selector selector, SocketChannel socket) throws IOException {

            this.selector = selector;
            this.socket = socket;
            // 非阻塞模式
            this.socket.configureBlocking(false);
            // select 和 register 方法都会请求 publicKeys，导致 register 阻塞
            selector.wakeup();
            this.key = this.socket.register(selector, SelectionKey.OP_READ);
            key.attach(this);
        }

        @SneakyThrows
        @Override
        public void run() {

            switch (state) {
                case READING:
                    read();
                    break;
                case WRITING:
                    send();
                    break;
                default:
                    System.out.println("Handler.run not in state");
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

            System.out.println("Handler.send:\t" + content);
            key.interestOps(SelectionKey.OP_READ);
            state = READING;
        }

        /**
         * 阻塞至数据读取完毕
         *
         * @author qingju.yao
         * @date 2020/7/8
         */
        private void read() throws IOException {

            synchronized (lock) {
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

                System.out.println("Handler.read:\t" + content);
                state = PROCESSING;
                executor.execute(new Processor());
            }
        }

        private void process() {

            synchronized (lock) {
                System.out.println("Handler.process:\t" + content);
                key.interestOps(SelectionKey.OP_WRITE);
                state = WRITING;
                // 状态更新后，及时唤醒；否则会阻塞至客户端断开时才响应
                selector.wakeup();
            }
        }

        class Processor implements Runnable {

            @Override
            public void run() {
                Handler.this.process();
            }
        }
    }
}
