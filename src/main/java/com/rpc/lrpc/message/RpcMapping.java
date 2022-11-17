package com.rpc.lrpc.message;

import com.rpc.lrpc.Annotation.RPCMapping;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RpcMapping {
    String mapping;
    Class<?> returnType;
    Class<?>[] paramType;

    Class<?> clazz;
    String name;

    Method source;


    public RpcMapping(Method method) {
        if (method.isAnnotationPresent(RPCMapping.class))
        {
            mapping=method.getAnnotation(RPCMapping.class).value();
            returnType=method.getReturnType();
            paramType=method.getParameterTypes();
            clazz=method.getDeclaringClass();
            name=method.getName();
            source=method;
        }
        else {
            throw new RuntimeException();
        }
    }

    public RpcMapping() {
    }
}
