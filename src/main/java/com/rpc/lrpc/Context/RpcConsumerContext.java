package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
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
     final Set<RpcURL> urls=new CopyOnWriteArraySet<>();

    @Override
    public void addService(RpcService service,RpcURL rpcURL) {
        serviceMap.put(service.getServiceName(),service);
        mappings.addAll(Arrays.asList(service.getRpcMappings()));
        urls.add(rpcURL);
    }

    @Override
    public void addServices(Map<RpcService, RpcURL[]> service) {
        for (Map.Entry<RpcService, RpcURL[]> entry : service.entrySet()) {
            for (RpcURL rpcURL : entry.getValue()) {
                addService(entry.getKey(),rpcURL);
            }
        }
    }



    @Override
    public RpcMapping getRpcMappingByName(String mapping) {
        Optional<RpcMapping> first = mappings.stream().filter(x -> x.getMapping().equals(mapping)).findFirst();
        return first.orElse(null);
    }


}
