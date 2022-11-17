package com.rpc.lrpc.message;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.Annotation.RPCMapping;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;

@Data
public class RpcController {

    String beanName;
    Class<?> beanClass;
    RpcMapping[] rpcMappings;
    String serviceName;

    public RpcController(Class<?> beanClass, String serviceName,String beanName) {
        this.beanClass = beanClass;
        this.serviceName = serviceName;
        this.beanName=beanName;
        if (!beanClass.isAnnotationPresent(RPCController.class)) {
            throw new RuntimeException("这个不是RPCController");
        }
        ArrayList<RpcMapping> mappings = new ArrayList<>();
        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RPCMapping.class)) {
                mappings.add(new RpcMapping(method));
            }
        }
    }

    public RpcController() {
    }
}
