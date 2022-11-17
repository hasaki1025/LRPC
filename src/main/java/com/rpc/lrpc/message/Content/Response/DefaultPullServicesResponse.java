package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

@Data
public class DefaultPullServicesResponse implements PullServicesResponse {
    //所有service
    RpcURL[] services;
    //单个Serivice
    RpcURL[] service;

    @Override
    public Object getValue() {
        return services;
    }
}
