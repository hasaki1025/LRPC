package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultPushServicesRequest implements PushServicesRequest {

    private final Map<String,RpcService> serviceMap=new HashMap<>();
    private final Set<RpcURL> rpcURLS=new HashSet<>();
    @Override
    public RpcURL[] getAllServiceUrl() {
        return rpcURLS.toArray(rpcURLS.toArray(new RpcURL[0]));
    }

    @Override
    public RpcURL[] getServiceUrl(String serviceName) {
        Stream<RpcURL> stream = rpcURLS.stream().filter((x) -> x.getServiceName().equals(serviceName));
        return stream.toArray(RpcURL[]::new);
    }

    @Override
    public RpcService[] getAllRpcService() {
        return serviceMap.values().toArray(new RpcService[0]);
    }

    @Override
    public RpcService getRpcService(String serviceName) {
        return serviceMap.get(serviceName);
    }
}
