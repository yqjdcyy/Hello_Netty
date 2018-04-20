package com.yao.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2014/12/18.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(EchoServerHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString());
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
