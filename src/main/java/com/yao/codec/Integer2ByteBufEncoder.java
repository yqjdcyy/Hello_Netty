package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Encode JavaObject to ByteBuf
 *
 * @author qingju.yao
 * @date 2020/07/04
 */
public class Integer2ByteBufEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        out.writeInt((Integer) msg);
        System.out.println("Integer2ByteBufEncoder.encode\t" + msg);
    }
}
