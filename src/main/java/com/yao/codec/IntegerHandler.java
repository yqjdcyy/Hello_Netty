package com.yao.codec;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Print Msg(Integer)
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
@Slf4j
public class IntegerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        assert msg instanceof Integer;

        Integer in = (Integer) msg;
        System.out.println(IntegerHandler.class.getSimpleName() + "\t" + in);

        super.channelRead(ctx, msg);
    }
}
