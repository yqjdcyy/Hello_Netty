package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Read Integer from ByteBuf without counting
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
public class Byte2IntegerReplayDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int input = in.readInt();
        System.out.println(Byte2IntegerDecoder.class.getSimpleName() + "\t" + input);
        out.add(input);
    }
}
