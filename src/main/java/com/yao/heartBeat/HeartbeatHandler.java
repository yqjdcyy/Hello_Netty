package com.yao.heartBeat;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Administrator on 2015/1/19.
 */
public class HeartbeatHandler extends ChannelHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if(evt instanceof IdleStateEvent){
            //TODO 心跳处理
            IdleStateEvent ise= (IdleStateEvent) evt;
            if(ise.state()== IdleState.WRITER_IDLE){
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
