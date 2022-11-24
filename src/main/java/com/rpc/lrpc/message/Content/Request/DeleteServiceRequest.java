package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcAddress;

public interface DeleteServiceRequest extends RequestContent{
    RpcAddress getAddress();
}
