package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * 服务提供者基本接口
 */
public interface RPCServiceProvider {

    /**
     * 获取本地服务名称
     * @return 本地服务名称
     */
    String getServiceName();

    /**
     * 返回该服务绑定端口
     * @return 服务端口
     */
    int getPort();

    /**
     * 获取RPC服务包装类
     * @return RPCService包装类
     */
    RpcService getRpcService();

    /**
     * 初始化服务提供者
     */
    void init();

    /**
     * 返回注册中心
     * @return
     */
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
