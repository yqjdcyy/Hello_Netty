package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessage 方式实现 StateReplayDecoder
 * 更推荐，效率相对更高
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
public class StateDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (in.readableBytes() < 4) {
            System.out.println("State-1 fail");
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            System.out.println("State-2 fail");
            return;
        }

        byte[] content = new byte[length];
        in.readBytes(content);
        out.add(content);
    }
}
