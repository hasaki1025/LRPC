package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

@Data
public class DefaultUpdateServiceResponse implements UpdateServiceResponse {
    RpcURL[] service;
    String serviceName;

    @Override
    public Object getValue() {
        return service;
    }
}
