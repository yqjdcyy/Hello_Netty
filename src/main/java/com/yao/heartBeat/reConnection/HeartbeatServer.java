package com.yao.study.network.netty.heartBeat.reConnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * User: wlai
 * Date: 9/25/2014
 * Time: 15:32 PM
 */
public class HeartbeatServer {
    public static final Logger logger = LoggerFactory.getLogger(HeartbeatServer.class);

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup).localAddress(new InetSocketAddress(1000)).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
                    cp.addLast(new LoggingHandler(LogLevel.INFO));
                    cp.addLast(new LineBasedFrameDecoder(8192));
                    cp.addLast(new StringDecoder());
                    cp.addLast(new StringEncoder());
                    //     cp.addLast(new ChatMessageDispatchHandler());
                    cp.addLast("heartbeat", new IdleStateHandler(10, 10, 0));
                    cp.addLast("chatHandler", new HeartbeatHandler());
                }
            });

            ChannelFuture future = server.bind().sync();
            logger.info("===========Server started===========");
            future.channel().closeFuture().sync();
            logger.info("===========Server closed===========");
        } catch (InterruptedException e) {
            logger.error(e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String retMsg = (String) msg;
        if (!"PongRead".equals(retMsg) || !"PongWrite".equals(retMsg)) {
            ctx.close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.isFirst()) { //如果是第一次出现Idle，则发送心跳包进行检测，否则直接关闭连接
                if (event.state() == IdleState.READER_IDLE) {
                    System.out.println("no reader event for 60 seconds");
                    ctx.writeAndFlush("PingRead\n");
                } else if (event.state() == IdleState.WRITER_IDLE) {
                    System.out.println("no writer event for 60 seconds");
                    ctx.writeAndFlush("PingWrite\n");
                }
            } else {
                System.out.println("GoodBye");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}