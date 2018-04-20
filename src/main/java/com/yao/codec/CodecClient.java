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

import com.yao.utils.DateUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Simplistic telnet client.
 */
public final class CodecClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8023"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new CodecClientInitializer());

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
/*
            for (; ; ) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                // Sends the received line to the server.
                HashMap<String, Object> map= new HashMap<String, Object>();
                map.put("proc", "netty.text");
                map.put("data", line);
                lastWriteFuture = ch.writeAndFlush(map);

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }
*/

            //测试服务端处理代理是否会引起阻塞
            Thread.sleep(200);
            long start= System.currentTimeMillis();
            for(int i= 0; i< 5; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("proc", "netty.text");
                map.put("data", i);
                lastWriteFuture = ch.writeAndFlush(map);
                System.out.println(DateUtils.getCurTime()+ "\t send to server:"+ map);
                Thread.sleep(200);
            }
            ch.closeFuture().sync();
            long end= System.currentTimeMillis();
            System.out.println("\n\t"+ 5+ " invoke cost "+ (end- start));

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
