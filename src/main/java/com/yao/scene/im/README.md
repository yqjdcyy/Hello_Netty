

# ChatServer.pipeline
## Case-IM
- IMDecoder(ByteBuf->  IMMessage)
    - ctx.channel().pipeline().remove(this)   // 非 IM 链接则移除 IM 解析
    - new MessagePack().read(...)             // unpack bytes to IMMessage
- TerminalServerHandler(IMMessage->  TextWebSocketFrame)
    - decoder.decode(msg)                     // 解析请求内容
    - switch(IMMessage.type){                 // 根据消息类型进行相应消息通知（登入、聊天、礼物）
        - for(channel)
            - content= encoder.encode(request)
            - channel.writeAndFlush(new TextWebSocketFrame(content));
    - }

## Case-Http
- HttpServerCodec.HttpRequestDecoder(ByteBuf->  HttpRequest)
    - HttpRequestDecoder(ByteBuf->  HttpRequest)    // 解析 Http 请求
- HttpObjectAggregator
    - MessageAggregator(                        // 将消息块拼装成消息整体，实现 MessageToMessageDecoder
        - HttpObject+ HttpMessage+ HttpContent->  FullHttpMessage)        // 通过类型判断目前消息传递的进度，包括头部、内容和最后消息体
- ChunkedWriteHandler                         // write 时将 ctx 加入队列，flush 时再逐一取出、发送；有效地使用最小资源发送大文件
- HttpServerHandler                           // 解析请求路径，将文件按 response/ DefaultFileRegion 的顺序输出

## Case-WebSocket
- WebSocketServerProtocolHandler(Bytebuf->    WebSocketFrame)     // 解析 WebSocket 请求
- WebSocketServerHandler
    - MsgProcessor.sendMsg(TextWebSocketFrame)                    // 将 WebSocket 请求转化为对应的消息处理


# Decoded.Msg
```
// Login
Msg:	[LOGIN][1594967642203][Tony][WebSocket]
Request:	IMMessage{addr='null', cmd='LOGIN', time=1594967642203, online=0, sender='Tony', receiver='null', content='null'}

// Chat
Msg:	[CHAT][1594967733371][Tony] - [CHAT][1594967650135][Tony] - Code
Request:	IMMessage{addr='null', cmd='CHAT', time=1594967733371, online=0, sender='Tony] - [', receiver='null', content='Code'}    
```
