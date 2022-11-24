package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcRegister {

    Map<RpcService, RpcURL[]> getRpcServiceMap();

    void registerService(RpcService service,RpcURL rpcURL);

    RpcService getService(String serviceName);

    RpcURL[] getRpcUrls(RpcService rpcService);

    RpcURL[] getRpcUrlsByName(String serviceName);

    RpcURL[] getAllUrl();


}
