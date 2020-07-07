package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class StateReplayDecoderTest {


    @Test
    public void test() throws InterruptedException {

        EmbeddedChannel channel = new EmbeddedChannel(new StateReplayDecoder(), new BytesProcessHandler());

        byte[] bytes = this.getClass().getSimpleName().getBytes(CharsetUtil.UTF_8);
        int length = bytes.length;
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(length);
        buffer.writeBytes(Arrays.copyOfRange(bytes, 0, length / 2));
        buffer.writeBytes(Arrays.copyOfRange(bytes, length / 2, length));
        channel.writeInbound(buffer);

        TimeUnit.SECONDS.sleep(2);
    }
}