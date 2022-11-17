package com.rpc.lrpc.message;


import lombok.Data;

@Data
public class RpcService {
    String serviceName;
    RpcMapping[] rpcMappings;
}
