package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcURL {
    String host;
    String port;
    String serviceName;

    public RpcURL(String host, String port, String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public RpcURL() {
    }
}
