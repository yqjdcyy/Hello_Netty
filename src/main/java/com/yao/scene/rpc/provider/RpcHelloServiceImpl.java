package com.yao.scene.rpc.provider;

import com.yao.scene.rpc.api.IRpcHelloService;

public class RpcHelloServiceImpl implements IRpcHelloService {

    @Override
    public String hello(String name) {
        return "Hello " + name + "!";  
    }  
  
}  
