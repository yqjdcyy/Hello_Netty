package com.yao.heartBeat;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

/**
 * Created by Administrator on 2015/1/19.
 */
public class TCPServerHandler implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        //设置读取数据超时处理
        pipeline.addLast("readTimeOut",new ReadTimeoutHandler(new HashedWheelTimer(),10));
        pipeline.addLast("handler", (ChannelHandler) new TCPServerHandler());
        return pipeline;
    }
}
