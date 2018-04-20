package com.yao.nettyExample.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HeartbeatClient {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress("localhost", 1000));
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
//                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
//                //  ch.pipeline().addLast(new OutboundHandlerT());
//                ch.pipeline().addLast(new LineBasedFrameDecoder(8192));
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new StringDecoder());
//                ch.pipeline().addLast(new ClientHeartbeatHandler());
            }
        });
        try {
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
            System.out.println("===========Close==========");
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        } finally {
            group.shutdownGracefully();
        }
    }
}

class ClientHeartbeatHandler extends ChannelHandlerAdapter {
    private static int TIMER= 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("ClientHeartbeatHandler.start");
        String retMsg = (String) msg;
//        System.out.println("ClientHeartbeatHandler.MSG= " + retMsg);
        if (HeartbeatHandler.HEARTBEAT_REQUEST.equals(retMsg)) {
            if (++TIMER < 2) {
                System.out.println("ClientHeartbeatHandler.channelRead.TIMER= " + TIMER);
                ctx.writeAndFlush("ClientHeartbeatHandler.channelRead");
            } else {
                System.out.println("ClientHeartbeatHandler.channelRead.NO RETURN.");
            }
        } else {
            System.out.println(retMsg);
        }
    }

}