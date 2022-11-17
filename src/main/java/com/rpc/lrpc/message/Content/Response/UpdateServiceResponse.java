package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcURL;

public interface UpdateServiceResponse extends ResponseContent{

    RpcURL[] getService();
    String getServiceName();
}
