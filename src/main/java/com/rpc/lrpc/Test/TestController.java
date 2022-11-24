package com.rpc.lrpc.Test;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.Annotation.RPCMapping;

@RPCController
public class TestController {

    @RPCMapping("test")
    public String gg(String a)
    {
        System.out.println("shabi");
        return "gg";
    }
}
