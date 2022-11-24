package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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

     final Map<String,RpcService> serviceMap=new ConcurrentHashMap<>();

     final Set<RpcMapping> mappings=new HashSet<>();
     final Set<RpcAddress> urls=new CopyOnWriteArraySet<>();

    @Override
    public void addService(RpcService service, RpcAddress rpcAddress) {
        serviceMap.put(service.getServiceName(),service);
        mappings.addAll(Arrays.asList(service.getRpcMappings()));
        urls.add(rpcAddress);
    }

    @Override
    public void addServices(Map<RpcService, RpcAddress[]> service) {
        for (Map.Entry<RpcService, RpcAddress[]> entry : service.entrySet()) {
            for (RpcAddress rpcAddress : entry.getValue()) {
                addService(entry.getKey(), rpcAddress);
            }
        }
    }



    @Override
    public RpcMapping getRpcMappingByName(String mapping) {
        Optional<RpcMapping> first = mappings.stream().filter(x -> x.getMapping().equals(mapping)).findFirst();
        return first.orElse(null);
    }

    @Override
    public boolean containAddress(RpcAddress rpcAddress) {
        return urls.contains(rpcAddress);
    }

    @Override
    public void removeAddress(RpcAddress rpcAddress) {
        if (urls.contains(rpcAddress)) {
            urls.remove(rpcAddress);
            if (rpcAddress.getServiceName()!=null
                    && !"".equals(rpcAddress.getServiceName())
                    && serviceMap.containsKey(rpcAddress.getServiceName())) {
                RpcService service = serviceMap.get(rpcAddress.getServiceName());
                if (urls.stream().noneMatch(x -> x.getServiceName().equals(rpcAddress.getServiceName()))) {
                    serviceMap.remove(service.getServiceName(),service);
                    List.of(service.getRpcMappings()).forEach(mappings::remove);
                }
            }
        }
    }


}
