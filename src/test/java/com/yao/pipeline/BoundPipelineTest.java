package com.yao.pipeline;

import com.yao.embedded.ChannelInputHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class BoundPipelineTest {

    @Test
    public void test() {

        final ChannelInputHandler handler = new ChannelInputHandler();
        final ChannelInitializer<EmbeddedChannel> initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new BoundPipeline.InboundChannelHandlerOne())
                        .addLast(new BoundPipeline.InboundChannelHandlerTwo())
                        .addLast(new BoundPipeline.InboundChannelHandlerThree());
            }
        };
        final EmbeddedChannel channel = new EmbeddedChannel(initializer);
        final ByteBuf buffer = Unpooled.buffer();

        buffer.writeBytes("1\n2".getBytes(StandardCharsets.UTF_8));
        channel.writeInbound(buffer);
        channel.flush();
        channel.writeOutbound(buffer);
        channel.close();
    }
}