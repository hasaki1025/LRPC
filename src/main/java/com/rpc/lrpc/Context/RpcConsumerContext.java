package com.rpc.lrpc.Context;

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


     final Map<String,RpcService> serviceMap=new ConcurrentHashMap<>();
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
            urls.addAll(List.of(map.get(service)));
        }
    }

    @Override
    public void UpdateServices(RpcService rpcService, RpcURL rpcURL) {
        serviceMap.put(rpcService.getServiceName(),rpcService);
        urls.add(rpcURL);
    }


}
