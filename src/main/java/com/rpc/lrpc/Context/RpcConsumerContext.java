package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;
@Data
public class RpcConsumerContext implements RpcConsumer {

    @Value("${RPC.Server.Host}")
    final String registerServerHost;
    @Value("${RPC.Server.port}")
    final Integer registerServerPort;

     final Map<String,RpcService> serviceMap=new HashMap<>();
     final Set<RpcURL> urls=new HashSet<>();

    @Override
    public RpcService[] getRpcServices() {
        return serviceMap.values().toArray(new RpcService[0]);
    }

    @Override
    public RpcURL[] getRpcServiceURL(String serviceName) {
        return urls.stream().filter(x->x.getServiceName().equals(serviceName)).toArray(RpcURL[]::new);
    }

    @Override
    public RpcService getRpcService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    @Override
    //消费服务
    public Object comsumer(String serviceName, String mapping, Object[] params) {
        return null;
    }

    @Override
    //拉取服务列表
    public void PullServices(Map<RpcService,RpcURL[]> map) {
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
