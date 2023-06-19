package com.rpc.lrpc.Context;

import com.rpc.lrpc.LoadBalance.LoadBalancePolicy;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * RpcConsumer基本实现类,基于配置自动注入
 */
@Data
@ConditionalOnProperty(name = {
        "RPC.Server.port","RPC.Server.Host"
})
@Component
public class RpcConsumerContext implements RpcConsumer {

    @Value("${RPC.Server.Host}")
     String registerServerHost;
    @Value("${RPC.Server.port}")
     int registerServerPort;

    @Value("${RPC.Config.RequestTimeOut}")
    long requestTimeOut;
    @Value("${RPC.Config.LoadBalancePolicy:com.rpc.lrpc.LoadBalance.RoundRobin}")
    String loadBalancePolicy;

    /**
     * 保存所有rpcServices
     */
    private volatile Set<String> rpcServices=new CopyOnWriteArraySet<>();
    /**
     * 关于service和实际地址的映射
     */
    private volatile  Map<String,RpcAddress[]> addressMap=new ConcurrentHashMap<>();
    /**
     * 关于service到mappings映射
     */
    private volatile Map<String,String[]> mappingMap=new ConcurrentHashMap<>();
    /**
     * service对应的负载均衡策略
     */
    private volatile Map<String, LoadBalancePolicy> loadBalanceMap=new ConcurrentHashMap<>();

    @Override
    public void addService(RpcAddress rpcAddress, String[] mappings) throws Exception {
        String serviceName = rpcAddress.getServiceName();
        if (rpcServices.contains(serviceName))
        {
            List<RpcAddress> list = new ArrayList<>(List.of(addressMap.get(serviceName)));
            list.add(rpcAddress);
            addressMap.put(serviceName,list.toArray(new RpcAddress[0]));
        }
        else {
            Class<?> loadBalanceClass = Class.forName(loadBalancePolicy);
            Object o = loadBalanceClass.getDeclaredConstructor().newInstance();
            loadBalanceMap.put(serviceName, (LoadBalancePolicy) o);


            rpcServices.add(serviceName);
            addressMap.put(serviceName,new RpcAddress[]{rpcAddress});
            mappingMap.put(serviceName,mappings);
        }

    }

    @Override
    public void addServices(Map<String, RpcAddress[]> addressMap, Map<String, String[]> mappingMap) throws Exception{
        for (Map.Entry<String, RpcAddress[]> entry : addressMap.entrySet()) {
            for (RpcAddress address : entry.getValue()) {
                addService(address,mappingMap.get(address.getServiceName()));
            }
        }
    }

    @Override
    public RpcAddress[] getAllAddress() {
        HashSet<RpcAddress> addresses = new HashSet<>();
        for (RpcAddress[] value : addressMap.values()) {
            addresses.addAll(List.of(value));
        }
        return addresses.toArray(new RpcAddress[0]);
    }


    @Override
    public void removeAddress(RpcAddress rpcAddress) {
        assert rpcAddress.getServiceName()!=null && !"".equals(rpcAddress.getServiceName()) : "ServiceName can not be Null";

        Optional<String> first = rpcServices.stream().filter(x -> x.equals(rpcAddress.getServiceName())).findFirst();
        if (first.isEmpty())
        {
            return;
        }

        String serviceName = first.get();
        RpcAddress[] addresses = addressMap.get(serviceName);
        if ( addresses.length==0 || (addresses.length==1 && addresses[0].equals(rpcAddress))) {
            rpcServices.remove(serviceName);
            addressMap.remove(serviceName);
            mappingMap.remove(serviceName);
        }
        else {
            List<RpcAddress> list = new ArrayList<>(List.of(addresses));
            list.remove(rpcAddress);
            addressMap.put(serviceName,list.toArray(new RpcAddress[0]));
        }
    }

    @Override
    public RpcAddress getRpcAddress(String serviceName) {
        LoadBalancePolicy policy = loadBalanceMap.get(serviceName);
        if (policy == null)
        {
            return null;
        }

        return policy.getNext(addressMap.get(serviceName));
    }

    @Value("${RPC.Config.ChannelType}")
    String channelType;

    @Override
    public String getChannelType() {
        return channelType;
    }



}
