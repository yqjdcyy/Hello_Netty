package com.yao.heartBeat;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Administrator on 2015/1/19.
 */
public class Heartbeat extends IdleStateAwareChannelHandler {

    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
        super.channelIdle(ctx, e);

        if (e.getState() == IdleState.READER_IDLE) {
            byte[] test = " ac0bce0490007050006".getBytes();
            ChannelBuffer channelBuffer = ChannelBuffers.buffer(test.length);
            channelBuffer.writeBytes(test);
            //发送超时数据到终端.
            e.getChannel().write(channelBuffer);
        }
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        IdleStateEvent ise = (IdleStateEvent) e;
        if (ise.getState() == IdleState.READER_IDLE) {
            byte[] test = "超时 ...".getBytes();
            ChannelBuffer channelBuffer = ChannelBuffers.buffer(test.length);
            channelBuffer.writeBytes(test);
            //发送超时数据到终端.
            ctx.getChannel().write(channelBuffer);
        }

        super.handleUpstream(ctx, e);
    }
}
