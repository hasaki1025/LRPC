package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
@Component
public class RpcRegisterContext implements RpcRegister {
     final Map<String,RpcService> rpcServiceMap=new HashMap<>();
     final Set<RpcURL> rpcURLS=new HashSet<>();

     @Value("${RPC.Register.port}")
      Integer port;

    @Override
    public RpcService[] getRpcServices() {
        return rpcServiceMap.values().toArray(new RpcService[0]);
    }

    @Override
    public RpcURL[] getRpcServiceURL(String serviceName) {
        return rpcURLS.stream().filter(x->x.getServiceName().equals(serviceName)).toArray(RpcURL[]::new);
    }

    @Override
    public RpcService getRpcService(String serviceName) {
        return rpcServiceMap.get(serviceName);
    }

    @Override
    public RpcURL[] getAllRpcURL() {
        return rpcURLS.toArray(new RpcURL[0]);
    }


    @Override
    public void registerService(RpcService rpcService, RpcURL rpcURL) {
        rpcServiceMap.put(rpcService.getServiceName(),rpcService);
        rpcURLS.add(rpcURL);
    }

    @Override
    public void removeURL(RpcURL rpcURL) {
        RpcURL[] urls = getRpcServiceURL(rpcURL.getServiceName());
        if (urls.length==1) {
            if (urls[0].equals(rpcURL))
            {

                rpcServiceMap.remove(rpcURL.getServiceName());
            }
            else {
                throw new RuntimeException("Not exist URL.....");
            }
        }
        else {
            rpcURLS.remove(rpcURL);
        }
    }

    @Override
    public void addURL(RpcURL rpcURL) {
        rpcURLS.add(rpcURL);
    }
}
