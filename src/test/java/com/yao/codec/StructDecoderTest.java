package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * 结构化数据解析
 * <pre>
 * |version: byte[4]| length: int(4)| check: byte[1]| content: byte[length]|
 * </pre>
 *
 * @author qingju.yao
 * @date 2020/07/04
 */
public class StructDecoderTest {


    @Test
    public void test() throws InterruptedException {

        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 4, 4, 1, 9),
                new StringDecoder());

        // input
        String className = this.getClass().getSimpleName();
        byte[] bytes = className.getBytes(CharsetUtil.UTF_8);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes("0001".getBytes(CharsetUtil.UTF_8));
        buffer.writeInt(bytes.length);
        buffer.writeByte(0);
        buffer.writeBytes(bytes);
        channel.writeInbound(buffer);

        // Output
        Object retObj = null;
        while (null != (retObj = channel.readInbound())) {
            System.out.println("Write:\t" + retObj);
        }

        TimeUnit.SECONDS.sleep(2);
    }
}
