package com.yao.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

/**
 * @author qingju.yao
 * @date 2020/07/07
 */
public class PkgClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(PkgClientHandler.class.getName());

    private int pkgCnt = 100;
    private byte[] bytes;

    public PkgClientHandler() {
        bytes = getClass().getSimpleName().getBytes(CharsetUtil.UTF_8);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ByteBuf buf;
        for (int i = 0; i < pkgCnt; i++) {
            buf = Unpooled.buffer();
            buf.writeBytes(bytes);
            ctx.writeAndFlush(buf);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        logger.info("Client.receive:\t" + new String(bytes, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Client.error:\t" + cause);
        ctx.close();
    }
}
