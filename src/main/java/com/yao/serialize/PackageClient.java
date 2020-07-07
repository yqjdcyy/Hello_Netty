package com.yao.serialize;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

/**
 * 排查 Client 消息未成功发送
 *
 * @author qingju.yao
 * @date 2020/7/7
 */
public class PackageClient {

    private static final Logger logger = Logger.getLogger(PackageClient.class.getName());

    private final String host;
    private final int port;
    private final int pkgCnt = 100;

    public static void main(String[] args) {
        try {
            new PackageClient("localhost", 9999).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PackageClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            logger.info("Client.childHandler init");
                            // PackageClientHandler 直接以类重写于此处，无法运行
                            // 初步怀疑是运行的时机，后续可看一下 JVM 原理后再串联起来
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                    byte[] bytes = ChannelHandlerAdapter.class.getSimpleName().getBytes(CharsetUtil.UTF_8);
                                    for (int i = 0; i < pkgCnt; i++) {
                                        ByteBuf buffer = Unpooled.buffer();
                                        buffer.writeBytes(bytes);
                                        ctx.writeAndFlush(buffer);
                                    }
                                    logger.info(System.currentTimeMillis() + "");
                                    logger.info("Client.Active");
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();

            // 但于连接后发送，则可正常发送
            byte[] bytes = ChannelHandlerAdapter.class.getSimpleName().getBytes(CharsetUtil.UTF_8);
            for (int i = 0; i < pkgCnt; i++) {
                ByteBuf buffer = Unpooled.buffer();
                buffer.writeBytes(bytes);
                f.channel().writeAndFlush(buffer);
            }
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public class PackageClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            byte[] bytes = this.getClass().getSimpleName().getBytes(CharsetUtil.UTF_8);
            for (int i = 0; i < pkgCnt; i++) {
                ByteBuf buffer = Unpooled.buffer();
                buffer.writeBytes(bytes);
                ctx.writeAndFlush(buffer);
            }
            logger.info("Client.Active");
            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
        }
    }
}
