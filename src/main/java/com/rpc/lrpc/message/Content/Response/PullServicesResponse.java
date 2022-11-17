package com.rpc.lrpc.message.Content.Response;



import com.rpc.lrpc.message.RpcURL;



public interface PullServicesResponse extends ResponseContent{

    RpcURL[] getServices();
    RpcURL[] getService();
}
