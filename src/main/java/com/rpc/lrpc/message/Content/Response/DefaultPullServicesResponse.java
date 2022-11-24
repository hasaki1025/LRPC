package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DefaultPullServicesResponse implements PullServicesResponse {

    Exception exception;
    final Map<RpcService, RpcAddress[]> rpcServiceMap=new HashMap<>();


    @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public void addRpcService(Map<RpcService, RpcAddress[]> map) {
        rpcServiceMap.putAll(map);
    }
}
