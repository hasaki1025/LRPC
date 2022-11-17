package com.rpc.lrpc.message;


import lombok.Data;

@Data
public class RpcService {
    String serviceName;
    RpcController[] controllers;

    public RpcService(String serviceName, RpcController[] controllers) {
        this.serviceName = serviceName;
        this.controllers = controllers;
    }

    public RpcService() {
    }
}
