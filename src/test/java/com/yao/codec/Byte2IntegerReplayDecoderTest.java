package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class Byte2IntegerReplayDecoderTest {

    private final int LIMIT = 2;

    @Test
    public void test() throws InterruptedException {

        EmbeddedChannel channel = new EmbeddedChannel(new Byte2IntegerReplayDecoder(), new IntegerHandler());

        for (int i = 0; i < LIMIT; i++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            channel.writeInbound(buffer);
            channel.flush();
        }

        TimeUnit.SECONDS.sleep(LIMIT);
    }
}