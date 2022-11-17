package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

public interface RpcConsumer {

    RpcService[] getRpcServices();
    RpcURL getRpcService(String serviceName);
    Object comsumer(String serviceName,String mapping,Object[] params);
    RpcService[] PullServices();
    RpcService UpdateServices();
}
