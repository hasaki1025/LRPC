package com.rpc.lrpc.message.Content.Response;



import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;



public interface PullServicesResponse extends ResponseContent{

    RpcURL[] getServiceURL(String serviceName);
    RpcURL[] getAllServiceURL();

    RpcService[] getAllRpcService();

    RpcService getRpcService(String serviceName);
}



