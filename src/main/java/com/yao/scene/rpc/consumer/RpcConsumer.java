package com.yao.scene.rpc.consumer;

import com.yao.scene.rpc.api.IRpcHelloService;
import com.yao.scene.rpc.api.IRpcService;
import com.yao.scene.rpc.consumer.proxy.RpcProxy;
import com.yao.scene.rpc.provider.RpcHelloServiceImpl;

public class RpcConsumer {

    public static void main(String[] args) {

        IRpcHelloService implService = RpcProxy.create(RpcHelloServiceImpl.class);
        System.out.println(implService.toString());

        IRpcHelloService rpcHello = RpcProxy.create(IRpcHelloService.class);
        System.out.println(rpcHello.hello("Tom老师"));

        IRpcService service = RpcProxy.create(IRpcService.class);
        System.out.println("8 + 2 = " + service.add(8, 2));
        System.out.println("8 - 2 = " + service.sub(8, 2));
        System.out.println("8 * 2 = " + service.mult(8, 2));
        System.out.println("8 / 2 = " + service.div(8, 2));
    }

}
