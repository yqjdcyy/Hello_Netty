package com.yao.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * send data with personal struct
 *
 * @author qingju.yao
 * @date 2020/07/03
 */
public class StateReplayDecoder extends ReplayingDecoder<StateReplayDecoder.Status> {

    private int length;
    private byte[] content;


    public StateReplayDecoder() {
        // 初始化阶段
        super(Status.T1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        switch (state()) {
            case T1:
                // 分段读取数据
                length = in.readInt();
                // 阶段切换
                checkpoint(Status.T2);
                break;
            case T2:
                content = new byte[length];
                in.readBytes(content);
                // 最终阶段的数据合并，和向下投递
                out.add(content);
                checkpoint(Status.T1);
                break;
            default:
                break;
        }
    }

    enum Status {
        // length, int
        T1,
        // bytes[length]
        T2;
    }
}
