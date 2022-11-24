package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnBean(RpcRegister.class)
public class RegisterServer extends Server{

    @Value("${RPC.Register.port}")
    int port;
    private boolean isInit=false;
    @Autowired
    RpcRegisterServerChannelInitializer rpcRegisterServerChannelInitializer;
    public void init()
    {
        if (!isInit)
        {
            isInit=true;
            super.init(port,rpcRegisterServerChannelInitializer);
        }

    }

}
