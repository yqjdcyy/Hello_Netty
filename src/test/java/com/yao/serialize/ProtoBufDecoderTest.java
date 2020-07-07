package com.yao.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.junit.Test;

public class ProtoBufDecoderTest {

    public static final String HOST = "localhost";
    public static final int PORT = 9999;


    @Test
    public void server() throws InterruptedException {
        new PkgServer(
                new ProtobufVarint32FrameDecoder(),
                new ProtoBufDecoder()
        ).bind(PORT);
    }

    @Test
    public void serverByOrigin() throws InterruptedException {
        new PkgServer(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufDecoder(UserProto.User.getDefaultInstance()),
                new ProtoBufServerHandler()
        ).bind(PORT);
    }

    @Test
    public void client() throws InterruptedException {
        new PkgClient(new ProtoBufClientHandler()).connect(HOST, PORT);
    }


    public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

        private UserProto.User user;

        public ProtoBufClientHandler() {
            UserProto.User.Builder builder = UserProto.User.newBuilder();
            builder.setId(1);
            builder.setName(this.getClass().getSimpleName());
            user = builder.build();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            byte[] bytes = user.toByteArray();
            for (int i = 0; i < 100; i++) {
                ByteBuf buffer = Unpooled.buffer();
                buffer.writeShort(bytes.length);
                buffer.writeBytes(bytes);
                ctx.writeAndFlush(buffer);
            }

            super.channelActive(ctx);
        }
    }

    public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("ProtoBufServerHandler.receive:\t" + msg.toString());
            super.channelRead(ctx, msg);
        }
    }
}