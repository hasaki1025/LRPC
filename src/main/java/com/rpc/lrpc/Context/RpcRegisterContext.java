package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;

@Data
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
@Component
public class RpcRegisterContext implements RpcRegister {
     final Map<RpcService,RpcURL[]> rpcServiceMap=new HashMap<>();

     @Value("${RPC.Register.port}")
      Integer port;

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
        }
    }
}
