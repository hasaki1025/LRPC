package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import com.rpc.lrpc.net.DokiDokiMap;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Data
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
@Component
public class RpcRegisterContext implements RpcRegister {
     final Map<RpcService,RpcURL[]> rpcServiceMap=new ConcurrentHashMap<>();
     final Map<String,RpcService> serviceNameMap=new ConcurrentHashMap<>();

     @Value("${RPC.Register.port}")
      Integer port;

     //TODO 心跳功能实现
     @Autowired
    DokiDokiMap dokiDokiMap;

    @Override
    public void registerService(RpcService service, RpcURL rpcURL) {
        if (rpcServiceMap.containsKey(service))
        {
            ArrayList<RpcURL> urls = new ArrayList<>(List.of(rpcServiceMap.get(service)));
            urls.add(rpcURL);
            rpcServiceMap.put(service,urls.toArray(new RpcURL[0]));
        }
        else {
            rpcServiceMap.put(service,new RpcURL[]{rpcURL});
            serviceNameMap.put(service.getServiceName(),service);
        }
    }

    @Override
    public RpcService getService(String serviceName) {
        return serviceNameMap.get(serviceName);
    }

    @Override
    public RpcURL[] getRpcUrls(RpcService rpcService) {
        return rpcServiceMap.get(rpcService);
    }

    @Override
    public RpcURL[] getRpcUrlsByName(String serviceName) {
        return rpcServiceMap.get(serviceNameMap.get(serviceName));
    }
}
