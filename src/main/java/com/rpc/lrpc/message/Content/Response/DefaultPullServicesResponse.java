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

    Exception exception;
    final Map<RpcService, RpcURL[]> rpcServiceMap=new HashMap<>();


    @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public void addRpcService(Map<RpcService, RpcURL[]> map) {
        rpcServiceMap.putAll(map);
    }
}
