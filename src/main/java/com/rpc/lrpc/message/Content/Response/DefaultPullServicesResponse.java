package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class DefaultPullServicesResponse implements PullServicesResponse {

    Exception exception;
    final Set<RpcService> rpcServices=new HashSet<>();
    final Set<RpcAddress> rpcAddressSet=new HashSet<>();


    @Override
    public boolean hasException() {
        return exception!=null;
    }



    @Override
    public void addRpcService(Map<RpcService, RpcAddress[]> map) {
        for (Map.Entry<RpcService, RpcAddress[]> entry : map.entrySet()) {
            rpcServices.add(entry.getKey());
            rpcAddressSet.addAll(List.of(entry.getValue()));
        }
    }
}
