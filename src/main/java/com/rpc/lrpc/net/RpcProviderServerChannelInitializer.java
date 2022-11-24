package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RPCServiceProvider.class)
public class RpcProviderServerChannelInitializer extends RpcServerChannelInitializer {

}
