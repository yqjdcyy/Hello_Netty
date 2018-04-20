package com.yao.codec;

import com.yao.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/1/4.
 */
public class RPCClientEncoder extends MessageToByteEncoder<HashMap> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HashMap msg, ByteBuf out) throws Exception {
        if(msg instanceof HashMap && ((HashMap) msg).size()== 2){
            HashMap<String, Object> map= (HashMap) msg;
            out.writeBytes(ByteUtils.encodeBytes(map.get("proc").toString(), map.get("data")));
        }else{
            out.writeBytes(msg.toString().getBytes());
        }
    }


}
