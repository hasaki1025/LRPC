package com.rpc.lrpc;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
public class RegisterController {


    @Autowired
    RpcRegister register;


    @GetMapping("/getServices")
    RpcAddress[] getServices()
    {
        return register.getAllAddress();
    }
}
