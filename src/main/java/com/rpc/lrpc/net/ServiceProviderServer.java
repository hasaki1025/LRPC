package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
@ConditionalOnBean(RPCServiceProvider.class)
@Component
public class ServiceProviderServer extends Server{

    @Value("${RPC.Provider.port}")
    int port;

    private boolean isInit=false;

    @Autowired
    RpcProviderServerChannelInitializer rpcProviderServerChannelInitializer;


    public void init() {
        if (!isInit)
        {
            isInit=true;
            super.init(port,rpcProviderServerChannelInitializer);
        }

    }
}
