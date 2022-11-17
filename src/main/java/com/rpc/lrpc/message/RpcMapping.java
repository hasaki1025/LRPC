package com.rpc.lrpc.message;

import lombok.Data;

@Data
public class RpcMapping {
    String mapping;
    Class<?> returnType;
    Class<?>[] paramType;

}
