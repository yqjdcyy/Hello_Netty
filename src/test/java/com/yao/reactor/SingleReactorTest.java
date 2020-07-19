package com.yao.reactor;

import com.yao.serialize.PkgClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SingleReactorTest {

    public final String HOST = "localhost";
    public final int PORT = 9999;


    @Test
    void server() throws IOException {
        new SingleReactor().bind(PORT);
    }


    @Test
    public void client() throws InterruptedException {
        new PkgClient(new SingleReactorHandler()).connect(HOST, PORT);
    }


    public static class SingleReactorHandler extends ChannelInboundHandlerAdapter {


        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(this.getClass().getSimpleName().getBytes());
            ctx.writeAndFlush(buffer);

            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);

            System.out.println("SingleReactorHandler.receive:\t"+ new String(bytes, CharsetUtil.UTF_8));
        }
    }
}