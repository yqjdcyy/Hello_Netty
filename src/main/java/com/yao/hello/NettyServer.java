package com.yao.hello;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 *  1.运行NettyServer
 *  2.cmd输入telnet 127.0.0.1 9999，并输入任一字符后关闭
 *
 * Created by Administrator on 2014/12/18.
 */
public class NettyServer {
    final static int port = 9999;

    public static void main(String[] args) {
        Server server = new Server();
        server.config(port);
        server.start();
    }
}

class Server {
    ServerBootstrap bootstrap;
    Channel parentChannel;
    InetSocketAddress localAddress;
    MyChannelHandler channelHandler = new MyChannelHandler();

    Server() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors
                .newCachedThreadPool()));
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.soLinger", 2);
        bootstrap.getPipeline().addLast("servercnfactory", channelHandler);
    }

    void config(int port) {
        this.localAddress = new InetSocketAddress(port);
    }

    void start() {
        parentChannel = bootstrap.bind(localAddress);
    }

    class MyChannelHandler extends SimpleChannelHandler {

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            System.out.println("Channel closed " + e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) throws Exception {
            System.out.println("Channel connected " + e);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            try {
                System.out.println("New message " + e.toString() + " from "
                        + ctx.getChannel());
                processMessage(e);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        private void processMessage(MessageEvent e) {
            Channel ch = e.getChannel();
            ch.write(e.getMessage());
        }
    }
}
