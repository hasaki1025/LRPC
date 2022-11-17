package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;

import java.util.Map;

public interface UpdateServiceRequest extends RequestContent{
    String getServiceName();
}
