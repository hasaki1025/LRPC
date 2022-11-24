package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcService;

public class DefaultDeleteServiceRequest implements DeleteServiceRequest {
    RpcAddress address;


    public DefaultDeleteServiceRequest(RpcAddress address) {
        this.address = address;
    }

    @Override
    public RpcAddress getAddress() {
        return null;
    }
}
