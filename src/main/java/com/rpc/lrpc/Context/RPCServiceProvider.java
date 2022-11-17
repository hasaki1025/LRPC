package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;

import java.util.Collection;
import java.util.List;

public interface RPCServiceProvider {

    String getServiceName();
    int getPort();
    RpcService getRpcService();
    void init();

    String getRegisterServerHost();

    int getRegisterServerPort();


    RpcMapping[] getMappings();

    void addMapping(Collection<RpcMapping> rpcMappings);
}
