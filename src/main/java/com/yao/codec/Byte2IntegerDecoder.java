package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Read Integer from ByteBuf
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
@Slf4j
public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (in.readableBytes() >= 4) {
            int input = in.readInt();
            System.out.println(Byte2IntegerDecoder.class.getSimpleName() + "\t" + input);
            out.add(input);
        }
    }
}
