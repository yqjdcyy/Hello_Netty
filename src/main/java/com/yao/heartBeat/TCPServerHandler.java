package com.yao.heartBeat;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/1/19.
 */
public class TCPServerHandler extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();
        //设置读取数据超时处理
        pipeline.addLast("readTimeOut", new ReadTimeoutHandler(10, TimeUnit.SECONDS));
        pipeline.addLast("handler", (ChannelHandler) new TCPServerHandler());
    }
}
