package com.yao.nettyExample.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatServer {
    public static Map<String, Boolean> map = new HashMap<String, Boolean>();

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup).localAddress(new InetSocketAddress(1000)).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
//                    cp.addLast(new LoggingHandler(LogLevel.INFO));
//                    cp.addLast(new LineBasedFrameDecoder(8192));
                    cp.addLast(new StringDecoder());
                    cp.addLast(new StringEncoder());
                    //     cp.addLast(new ChatMessageDispatchHandler());
                    cp.addLast("heartbeat", new IdleStateHandler(0, 0, 5));
                    cp.addLast("chatHandler", new HeartbeatHandler());
                }
            });

            ChannelFuture future = server.bind().sync();
            System.out.println("===========Server started===========");
            future.channel().closeFuture().sync();
            System.out.println("===========Server closed===========");
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    public static final String HEARTBEAT_REQUEST = "HEARTBEAT_REQUEST";

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HeartbeatHandler.handlerRemoved\t" + ctx.channel().id().asShortText());
        HeartbeatServer.map.put(ctx.channel().id().asShortText(), false);
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HeartbeatHandler.channelActive\t" + ctx.channel().id().asShortText());
        HeartbeatServer.map.put(ctx.channel().id().asShortText(), true);
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(HEARTBEAT_REQUEST).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        HeartbeatServer.map.put(future.channel().id().asShortText(), false);
                        System.out.println(future.channel().id().asShortText() + "\tHeartbeatHandler.userEventTriggered.writeAndFlush.operationComplete.FAIL!");
                        future.channel().close();
                    } else {
                        HeartbeatServer.map.put(future.channel().id().asShortText(), true);
                    }
                }
            });
        } else {
            super.userEventTriggered(ctx, evt);
        }
        System.out.println(HeartbeatServer.map.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}