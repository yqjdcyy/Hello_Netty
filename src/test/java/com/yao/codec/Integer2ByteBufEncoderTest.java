package com.yao.codec;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class Integer2ByteBufEncoderTest {


    @Test
    public void test() throws InterruptedException {

        EmbeddedChannel channel = new EmbeddedChannel(new Integer2ByteBufEncoder());

        channel.write(1);
        channel.flush();

        Object retObj = channel.readOutbound();
        System.out.println(retObj);

        TimeUnit.SECONDS.sleep(2);
    }
}