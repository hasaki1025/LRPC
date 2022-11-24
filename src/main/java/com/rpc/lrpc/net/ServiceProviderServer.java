package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
@ConditionalOnBean(RPCServiceProvider.class)
@Component
public class ServiceProviderServer extends Server{

    @Value("${RPC.Provider.port}")
    int port;


    void init() {
        super.init(port);
    }
}
