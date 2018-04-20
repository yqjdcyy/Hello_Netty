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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.Executors;

/**
 * Creates a newly configured {@link io.netty.channel.ChannelPipeline} for a new channel.
 */
public class CodecServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringEncoder ENCODER = new StringEncoder();
    private static final RPCServerDecoder DECODER = new RPCServerDecoder();
    private static final CodecServerHandler SERVER_HANDLER = new CodecServerHandler();
    private static final EventLoopGroup DECODER_GROUP = new NioEventLoopGroup(10, Executors.newFixedThreadPool(10));
    private static final EventLoopGroup HANDLER_GROUP = new NioEventLoopGroup(10, Executors.newFixedThreadPool(10));

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add the text line codec combination first,
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(DECODER_GROUP, DECODER, ENCODER);
        pipeline.addLast(HANDLER_GROUP, SERVER_HANDLER);

        // and then business logic.
    }
}
