package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcConsumer {

    Object comsumer(String serviceName,String mapping,Object[] params);
    void addServices(Map<RpcService,RpcURL[]> map);
    void UpdateServices(RpcService rpcService,RpcURL rpcURL);




}
