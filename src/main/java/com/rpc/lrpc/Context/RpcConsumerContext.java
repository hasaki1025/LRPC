package com.rpc.lrpc.Context;

import com.rpc.lrpc.LoadBalance.LoadBalancePolicy;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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

    private volatile Set<RpcService> rpcServices=new CopyOnWriteArraySet<>();
    private volatile  Map<String,RpcAddress[]> addressMap=new ConcurrentHashMap<>();
    private volatile Map<String,RpcMapping[]> mappingMap=new ConcurrentHashMap<>();
    private volatile Map<String, LoadBalancePolicy> loadBalanceMap=new ConcurrentHashMap<>();

    @Override
    public void addService(RpcService service, RpcAddress rpcAddress) throws Exception {
        if (rpcServices.contains(service))
        {
            List<RpcAddress> list = new ArrayList<>(List.of(addressMap.get(service.getServiceName())));
            list.add(rpcAddress);
            addressMap.put(service.getServiceName(),list.toArray(new RpcAddress[0]));
        }
        else {
            rpcServices.add(service);
            addressMap.put(service.getServiceName(),new RpcAddress[]{rpcAddress});
            mappingMap.put(service.getServiceName(),service.getRpcMappings());
        }
        Class<?> loadBalanceClass = Class.forName(loadBalancePolicy);
        Object o = loadBalanceClass.getDeclaredConstructor().newInstance();
        loadBalanceMap.put(service.getServiceName(), (LoadBalancePolicy) o);
    }

    @Override
    public void addServices(Map<RpcService, RpcAddress[]> service) throws Exception{
        for (Map.Entry<RpcService, RpcAddress[]> entry : service.entrySet()) {
            for (RpcAddress address : entry.getValue()) {
                addService(entry.getKey(),address);
            }
        }
    }

    @Override
    public RpcAddress[] getAllAddress() {
        Collection<RpcAddress[]> values = addressMap.values();
        ArrayList<RpcAddress> list = new ArrayList<>();
        values.forEach(x->list.addAll(List.of(x)));
        return list.toArray(new RpcAddress[0]);
    }


    @Override
    public void removeAddress(RpcAddress rpcAddress) {
        assert rpcAddress.getServiceName()!=null && !"".equals(rpcAddress.getServiceName()) : "ServiceName can not be Null";
        Optional<RpcService> first = rpcServices.stream().filter(x -> x.getServiceName().equals(rpcAddress.getServiceName())).findFirst();
        assert first.isPresent() :"can not find service to macth this address";
        RpcService service = first.get();
        RpcAddress[] addresses = addressMap.get(service.getServiceName());
        if ( addresses.length==0 || (addresses.length==1 && addresses[0].equals(rpcAddress))) {
            rpcServices.remove(service);
            addressMap.remove(service.getServiceName());
            mappingMap.remove(service.getServiceName());
        }
        else {
            List<RpcAddress> list = new ArrayList<>(List.of(addresses));
            list.remove(rpcAddress);
            addressMap.put(service.getServiceName(),list.toArray(new RpcAddress[0]));
        }
    }

    @Override
    public RpcAddress getRpcAddress(String serviceName) {
        return loadBalanceMap.get(serviceName).getNext(addressMap.get(serviceName));
    }

    @Value("${RPC.Config.ChannelType}")
    String channelType;

    @Override
    public String getChannelType() {
        return channelType;
    }
}
