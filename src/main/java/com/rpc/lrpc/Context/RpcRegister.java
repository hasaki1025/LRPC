package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Map;

public interface RpcRegister {

    Map<RpcService, RpcAddress[]> getRpcServiceMap();

    void registerService(RpcService service, RpcAddress rpcAddress);

    RpcService getService(String serviceName);

    RpcAddress[] getRpcUrls(RpcService rpcService);

    RpcAddress[] getRpcUrlsByName(String serviceName);

    RpcAddress[] getAllUrl();




}
