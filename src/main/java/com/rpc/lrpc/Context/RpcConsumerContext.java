package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Data
@ConditionalOnProperty(name = {
        "RPC.Server.port","RPC.Server.Host"
})
@Component
public class RpcConsumerContext implements RpcConsumer {

    @Value("${RPC.Server.Host}")
     String registerServerHost;
    @Value("${RPC.Server.port}")
     Integer registerServerPort;

    @Value("${RPC.Config.RequestTimeOut}")
    long requestTimeOut;


     final Map<String,RpcService> serviceMap=new ConcurrentHashMap<>();

     final Set<RpcMapping> mappings=new HashSet<>();
     final Set<RpcURL> urls=new CopyOnWriteArraySet<>();
    @Override
    //消费服务
    public Object comsumer(String serviceName, String mapping, Object[] params) {
        return null;
    }

    @Override
    //拉取服务列表
    public void addServices(Map<RpcService,RpcURL[]> map) {
        Set<RpcService> serviceSet = map.keySet();
        for (RpcService service : serviceSet) {
            serviceMap.put(service.getServiceName(),service);
            mappings.addAll(List.of(service.getRpcMappings()));
            urls.addAll(List.of(map.get(service)));
        }
    }

    @Override
    public void updateServices(RpcService rpcService, RpcURL rpcURL) {
        serviceMap.put(rpcService.getServiceName(),rpcService);
        urls.add(rpcURL);
    }

    @Override
    public RpcMapping getRpcMappingByName(String mapping) {
        Optional<RpcMapping> first = mappings.stream().filter(x -> x.getMapping().equals(mapping)).findFirst();
        return first.orElse(null);
    }


}
