package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcConsumer {

    RpcService[] getRpcServices();
    RpcURL[] getRpcServiceURL(String serviceName);
    RpcService getRpcService(String serviceName);
    Object comsumer(String serviceName,String mapping,Object[] params);
    void PullServices(Map<RpcService,RpcURL[]> map);
    void UpdateServices(RpcService rpcService,RpcURL rpcURL);


}
