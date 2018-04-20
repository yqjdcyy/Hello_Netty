package com.yao.codec;

import com.yao.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/1/4.
 */
public class RPCServerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() <= 8) {
            out.add("参数异常啊，你怎么破");
        } else {
            HashMap map = ByteUtils.decodeBytes(in.array());
            out.add(map);
        }
    }
}
