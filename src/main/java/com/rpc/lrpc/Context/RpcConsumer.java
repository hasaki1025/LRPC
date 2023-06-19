package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Consumer基本接口类
 */
public interface RpcConsumer {


    /**
     * @return 注册中心地址
     */
    String getRegisterServerHost();

    /**
     * @return 注册中心端口号
     */
    int getRegisterServerPort();

    /**
     * 新增一个本地Service地址
     * @param rpcAddress RPC地址包装类
     * @param mappings 该服务的所有mapping映射
     * @throws Exception 负载均衡策略示例创建
     */
    void addService(RpcAddress rpcAddress, String[] mappings) throws Exception;

    /**
     * 增加多条Service记录
     * @param addressMap Service映射到实际地址
     * @param mappingMap Service映射到该Service的mapping
     * @throws Exception addService方法的多次调用
     */
    void addServices(Map<String, RpcAddress[]> addressMap, Map<String, String[]> mappingMap) throws Exception;

    /**
     * @return 获取本地所有地址
     */
    RpcAddress[] getAllAddress();

    /**
     * @return 获取请求超时时间
     */
    public long getRequestTimeOut();


    /**
     * 删除指定RPC地址
     * @param rpcAddress rpc地址
     */
    void removeAddress(RpcAddress rpcAddress);

    /**
     * 通过负载均衡返回一个服务地址
     * @param serviceName 服务名称
     * @return 服务地址
     */
    RpcAddress getRpcAddress(String serviceName);


    /**
     * @return 返回信道类型
     */
    String getChannelType();
}
