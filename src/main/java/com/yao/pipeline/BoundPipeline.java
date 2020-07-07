package com.yao.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author qingju.yao
 * @date 2020/06/19
 */
public class BoundPipeline {

    static class InboundChannelHandlerOne extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("One\tRead");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("One\tReadComplete");
            super.channelReadComplete(ctx);
        }
    }

    static class InboundChannelHandlerTwo extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("Two\tRead");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Two\tReadComplete");
            super.channelReadComplete(ctx);
        }
    }

    static class InboundChannelHandlerThree extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("Three\tRead");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Three\tReadComplete");
            super.channelReadComplete(ctx);
        }
    }
}
