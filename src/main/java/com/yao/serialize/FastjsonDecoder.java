package com.yao.serialize;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 反序列化 Json 格式数据
 *
 * @author qingju.yao
 * @date 2020/07/06
 */
public class FastjsonDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

        int length = in.readableBytes();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        String content = new String(bytes, CharsetUtil.UTF_8);

        try {
            out.add(JSON.parseObject(bytes, JsonObj.class));
            System.out.println("FastjsonDecoder.receive:\t" + content);
        } catch (Exception e) {
            System.err.printf("FastjsonDecoder fail to parse String[%s] to json: %s\n", content, e.getMessage());
        }
    }

    public static class JsonObj {
        private String name;

        public JsonObj(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("{");
            builder.append(" \"name\":\"").append(name).append('\"');
            builder.append('}');
            return builder.toString();
        }
    }
}
