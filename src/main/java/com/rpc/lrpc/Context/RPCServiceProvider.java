package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;

public interface RPCServiceProvider {

    String getServiceName();
    int getPort();
    RpcService getRpcService();

    void registerService();

    void dokidoki();
}
