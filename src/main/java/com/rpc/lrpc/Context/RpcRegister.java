package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Map;

/**
 * 注册中心基本接口
 */
public interface RpcRegister {


    /**
     * 注册服务
     * @param mappings 映射数组
     * @param rpcAddress 服务地址
     */
    void registerService(String[] mappings, RpcAddress rpcAddress);

    /**
     * 获取指定Service的所有实际地址
     * @param serviceName 服务名称
     * @return 实际地址数组
     */
    RpcAddress[] getRpcAddress(String serviceName);

    /**
     * @return 当前注册中心中保存的所有RPC地址
     */
    RpcAddress[] getAllAddress();

    /**
     * 删除指定地址
     * @param rpcAddress 地址
     */
    void removeAddress(RpcAddress rpcAddress);

    /**
     * 获取指定service所有mapping
     * @param serviceName 服务名称
     * @return 该服务名称所有mapping
     */
    String[] getMappings(String serviceName);

    /**
     * 返回所有服务名称
     * @return 所有服务名称
     */
    String[] getAllServiceName();



}
