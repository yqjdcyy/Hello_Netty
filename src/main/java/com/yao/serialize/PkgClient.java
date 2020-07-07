package com.yao.serialize;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author qingju.yao
 * @date 2020/07/07
 */
public class PkgClient {

    private ChannelHandler[] handlers;

    public PkgClient(ChannelHandler... handlers) {

        if (ArrayUtils.isEmpty(handlers)) {
            this.handlers = new ChannelHandler[]{new PkgClientHandler()};
        } else {
            this.handlers = handlers;
        }
    }

    public void connect(String host, int port) throws InterruptedException {

        EventLoopGroup boss = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(boss)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(handlers);
                        }
                    });
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        new PkgClient().connect(PkgServer.HOST, PkgServer.PORT);
    }
}
