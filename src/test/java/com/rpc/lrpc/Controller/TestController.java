package com.rpc.lrpc.Controller;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.Annotation.RPCMapping;
import org.springframework.stereotype.Component;

@RPCController("TestController")
@Component
public class TestController {
    @RPCMapping("/test")
    String test(String a)
    {
        System.out.println(a);
        return "123";
    }
}
