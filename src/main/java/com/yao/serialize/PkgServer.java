package com.yao.serialize;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author qingju.yao
 * @date 2020/07/07
 */
public class PkgServer {

    public static final int PORT = 10086;
    public static final String HOST = "localhost";

    private ChannelHandler[] handlers;

    public PkgServer(ChannelHandler... handlers) {

        if (ArrayUtils.isEmpty(handlers)) {
            this.handlers = new ChannelHandler[]{new PkgServerHandler()};
        } else {
            this.handlers = handlers;
        }
    }

    public void bind(int port) throws InterruptedException {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            ChannelFuture sync = bootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 要解决粘包、半包问题，需添加 FixedLengthFrameDecoder 等实现
            ch.pipeline().addLast(handlers);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        new PkgServer().bind(PORT);
    }
}
