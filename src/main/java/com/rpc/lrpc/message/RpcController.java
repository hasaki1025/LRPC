package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcController {

    String beanName;
    Class<?> beanClass;
    RpcMapping[] rpcMappings;
    String serviceName;
}
