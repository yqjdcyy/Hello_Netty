package com.yao.serialize;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class FastjsonDecoderTest {

    public static final String HOST = "localhost";
    public static final int PORT = 9999;

    private Gson gson;

    @Before
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void server() throws InterruptedException {
        new PkgServer(
                new FixedLengthFrameDecoder(22),
                new FastjsonDecoder())
                .bind(PORT);
    }

    @Test
    public void serverWithStickPkg() throws InterruptedException {
        new PkgServer(
                new FastjsonDecoder())
                .bind(PORT);
    }

    @Test
    public void client() throws InterruptedException {
        new PkgClient(new JsonEncoder()).connect(HOST, PORT);
    }


    class JsonObjHandler extends MessageToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
            System.out.println(msg);
            System.out.println(out);
        }
    }

    public static class JsonEncoder extends ChannelInboundHandlerAdapter {

        private Gson gson;
        private int pkgCnt;

        public JsonEncoder() {
            this(100);
        }

        public JsonEncoder(int pkgCnt) {
            this.pkgCnt = pkgCnt;
            gson = new Gson();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            FastjsonDecoder.JsonObj obj = new FastjsonDecoder.JsonObj(this.getClass().getSimpleName());
            byte[] bytes = gson.toJson(obj).getBytes(CharsetUtil.UTF_8);
            for (int i = 0; i < pkgCnt; i++) {
                ByteBuf buf = Unpooled.copiedBuffer(bytes);
                ctx.writeAndFlush(buf);
            }

            super.channelActive(ctx);
        }
    }
}