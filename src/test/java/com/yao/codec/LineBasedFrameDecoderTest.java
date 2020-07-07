package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test LineBasedFrameDecoder
 *
 * @author qingju.yao
 * @date 2020/07/04
 */
public class LineBasedFrameDecoderTest {


    @Test
    public void test() throws InterruptedException {

        EmbeddedChannel channel = new EmbeddedChannel(new LineBasedFrameDecoder(100), new StringDecoder(CharsetUtil.UTF_8));

        // init
        String className = this.getClass().getName() + "\r\n";
        String input = className.replace(".", "\n");
        System.out.println("Input:\t" + input);

        // input
        byte[] bytes = input.getBytes(CharsetUtil.UTF_8);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(bytes);
        channel.writeInbound(buffer);

        // output
        Object retObj = null;
        while (null != (retObj = channel.readInbound())) {
            System.out.println("Output:\t" + retObj);
        }

        TimeUnit.SECONDS.sleep(2);
    }
}
