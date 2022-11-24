package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcUrl {
    RpcAddress address;
    String mapping;



    public RpcUrl(RpcAddress address, String mapping) {
        this.address = address;
        this.mapping = mapping;
    }
}
