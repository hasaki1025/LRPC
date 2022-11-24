package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcAddress {
    String host;
    int port;
    String serviceName="";

    public RpcAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcAddress(String host, int port, String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public RpcAddress() {
    }

    public RpcAddress(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "rpc://"+host+":"+port;
    }


}
