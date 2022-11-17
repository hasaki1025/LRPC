package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcURL {
    String host;
    String port;
    RpcService rpcService;
}
