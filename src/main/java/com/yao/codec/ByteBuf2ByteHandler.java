package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author qingju.yao
 * @date 2020/07/04
 */
public class ByteBuf2ByteHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] content = new byte[in.readableBytes()];
        in.readBytes(content);

        out.add(content);
    }
}
