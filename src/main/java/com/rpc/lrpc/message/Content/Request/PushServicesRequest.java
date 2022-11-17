package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

public interface PushServicesRequest extends RequestContent{

    RpcURL[] getAllServiceUrl();
    RpcURL[] getServiceUrl(String serviceName);
    RpcService[] getAllRpcService();
    RpcService getRpcService(String serviceName);
}
