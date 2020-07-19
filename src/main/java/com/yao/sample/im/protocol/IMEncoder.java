package com.yao.sample.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 自定义IM协议的编码器
 */
public class IMEncoder extends MessageToByteEncoder<IMMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out)
            throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }

    public String encode(IMMessage msg) {
        if (null == msg) {
            return "";
        }
        String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        // [LOGIN][1594967642203][Tony][WebSocket]
        if (IMP.LOGIN.getName().equals(msg.getCmd()) ||
                IMP.FLOWER.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");

            //[CHAT][1594967650135][Tony] - Code
        } else if (IMP.CHAT.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getSender() + "]");
        } else if (IMP.SYSTEM.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getOnline() + "]");
        }
        if (!(null == msg.getContent() || "".equals(msg.getContent()))) {
            prex += (" - " + msg.getContent());
        }
        return prex;
    }

}
