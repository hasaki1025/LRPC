package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public interface RPCServiceProvider {

    String getServiceName();
    int getPort();
    RpcService getRpcService();
    void init();

    String getRegisterServerHost();

    int getRegisterServerPort();


    RpcMapping[] getMappings();

    void addMapping(Collection<RpcMapping> rpcMappings);

    RpcMapping getMapping(String mapping);

    void addController(RpcController rpcController);

    RpcAddress getRpcUrl();

    Object invokeMapping(Object[] params,String mapping) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;


    String getChannelType();
    int getHeartGap();
}
