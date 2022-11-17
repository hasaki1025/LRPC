package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcRegister {
    RpcService[] getRpcServices();
    RpcURL[] getRpcServiceURL(String serviceName);
    RpcService getRpcService(String serviceName);

    RpcURL[] getAllRpcURL();
    void registerService(RpcService rpcService,RpcURL rpcURL);
    void removeURL(RpcURL rpcURL);
    void addURL(RpcURL rpcURL);

}
