package com.rpc.lrpc.message;

import com.rpc.lrpc.Annotation.RPCMapping;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RpcMapping {
    String mapping;
    Class<?> returnType;
    Class<?>[] paramType;


    public RpcMapping(Method method) {
        if (method.isAnnotationPresent(RPCMapping.class))
        {
            mapping=method.getAnnotation(RPCMapping.class).value();
            returnType=method.getReturnType();
            paramType=method.getParameterTypes();
        }
        else {
            throw new RuntimeException();
        }
    }

    public RpcMapping() {
    }
}
