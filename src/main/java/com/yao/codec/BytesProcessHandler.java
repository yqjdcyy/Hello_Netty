package com.yao.codec;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * print bytes in String
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
public class BytesProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        assert msg instanceof byte[];

        byte[] bytes = (byte[]) msg;
        System.out.println(BytesProcessHandler.class.getSimpleName() + ":\t" + new String(bytes, CharsetUtil.UTF_8));

        super.channelRead(ctx, msg);
    }
}
