package com.rpc.lrpc.message;


import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class RpcService {
    String serviceName;
    RpcController[] controllers;

    RpcMapping[] rpcMappings;

    public RpcService(String serviceName, RpcController[] controllers) {
        this.serviceName = serviceName;
        this.controllers = controllers;
        HashSet<RpcMapping> set = new HashSet<>();
        for (RpcController controller : controllers) {
            set.addAll(List.of(controller.getRpcMappings()));
        }
        rpcMappings=set.toArray(new RpcMapping[0]);
    }

    public RpcService() {
    }
}
