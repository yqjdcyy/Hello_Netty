package com.yao.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ProtoBuf Decoder
 * 可结合 ProtobufVarint32FrameDecoder 一并使用，进行包的拆解
 *
 * @author qingju.yao
 * @date 2020/07/07
 */
public class ProtoBufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        UserProto.User userProto = UserProto.User.parseFrom(bytes);

        System.out.println("ProtoBufDecoder.receive:\t" + userProto);
    }
}
