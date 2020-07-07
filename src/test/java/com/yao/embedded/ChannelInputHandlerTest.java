package com.yao.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class ChannelInputHandlerTest {

    @Test
    public void test() throws InterruptedException {

        // 未成功调用其初始化方法
//        final ChannelInitializer<EmbeddedChannel> initializer = new ChannelInitializer<EmbeddedChannel>() {
//            @Override
//            protected void initChannel(EmbeddedChannel ch) throws Exception {
//                ch.pipeline().addLast(handler);
//            }
//        };
//        final EmbeddedChannel channel = new EmbeddedChannel(initializer);


        final ChannelInputHandler handler = new ChannelInputHandler();
        final EmbeddedChannel channel = new EmbeddedChannel(handler);
        final ByteBuf buffer = Unpooled.buffer();

        buffer.writeInt(1);
        channel.writeInbound(buffer);
        channel.flush();
        channel.writeInbound(buffer);
        channel.flush();
        channel.close();

        TimeUnit.SECONDS.sleep(2);
        channel.finish();
    }

}