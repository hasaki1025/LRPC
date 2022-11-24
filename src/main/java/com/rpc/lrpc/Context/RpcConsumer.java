package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface RpcConsumer {

    void addService(RpcService service, RpcAddress rpcAddress) throws Exception;
    void addServices(Map<RpcService, RpcAddress[]> service) throws Exception;
    String getRegisterServerHost();
    int getRegisterServerPort();
    RpcAddress[] getAllAddress();

    public long getRequestTimeOut();

    void removeAddress(RpcAddress rpcAddress);

    /**
     * 通过负载均衡返回一个服务地址
     * @param serviceName 服务名称
     * @return 服务地址
     */
    RpcAddress getRpcAddress(String serviceName);


    String getChannelType();
}
