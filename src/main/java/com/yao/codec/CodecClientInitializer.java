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
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Creates a newly configured {@link io.netty.channel.ChannelPipeline} for a new channel.
 */
public class CodecClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final RPCClientEncoder ENCODER = new RPCClientEncoder();
    private static final StringDecoder DECODER = new StringDecoder();
    private static final CodecClientHandler CLIENT_HANDLER = new CodecClientHandler();

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // Add the text line codec combination first,
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(ENCODER);
        pipeline.addLast(DECODER);

        // and then business logic.
        pipeline.addLast(CLIENT_HANDLER);
    }
}
