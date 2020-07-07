/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.yao.codec;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;

/**
 * Handles a server-side channel.
 */
@Sharable
public class CodecServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object request) {
        // Generate and write a response.
        String response;
        boolean close = false;
        if (request instanceof HashMap) {
            response = request.toString();
        } else {
            response = "Error router, the request is not type of hashmap!\n\tmsg: " + request;
        }

        ctx.executor().execute(new ThreadHandler((HashMap)request));

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = ctx.writeAndFlush(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    class ThreadHandler implements Runnable{

        private Integer data;

        public ThreadHandler(HashMap map) {
            this.data= Integer.valueOf(map.get("data").toString());
        }

        @Override
        public void run() {
            try {
                if(this.data% 2== 0){
                    Thread.sleep(1000);
                    System.out.println("Wait for 1 mils!\t" + data+ "\t"+ Thread.currentThread().getId());
                }else{
                    System.out.println("Just no sleep\t"+ Thread.currentThread().getId());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
