package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class DefaultPullServicesResponse implements PullServicesResponse {

    private final Map<String,RpcService> rpcServiceMap=new HashMap<>();
    private final Set<RpcURL> rpcURLs=new HashSet<>();
    Exception e;

    @Override
    public RpcURL[] getServiceURL(String serviceName) {
        return rpcURLs.stream().filter(x->x.getServiceName().equals(serviceName)).toArray(RpcURL[]::new);
    }

    @Override
    public RpcURL[] getAllServiceURL() {
        return rpcURLs.toArray(new RpcURL[0]);
    }

    @Override
    public RpcService[] getAllRpcService() {
        return rpcServiceMap.values().toArray(new RpcService[0]);
    }

    @Override
    public RpcService getRpcService(String serviceName) {
        return rpcServiceMap.get(serviceName);
    }

    @Override
    public boolean hasException() {
        return e!=null;
    }

    @Override
    public Exception getException() {
        return e;
    }
}
