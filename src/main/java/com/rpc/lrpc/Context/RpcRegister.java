package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Map;

public interface RpcRegister {


    void registerService(String[] mappings, RpcAddress rpcAddress);

    RpcAddress[] getRpcAddress(String serviceName);

    RpcAddress[] getAllAddress();

    void removeAddress(RpcAddress rpcAddress);

    String[] getMappings(String serviceName);
    String[] getAllServiceName();



}
