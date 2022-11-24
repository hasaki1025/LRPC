package com.rpc.lrpc.message.Content.Response;



import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface PullServicesResponse extends ResponseContent {


    void addRpcService(Map<RpcService, RpcAddress[]> map);

    Set<RpcService> getRpcServices();

    Set<RpcAddress> getRpcAddressSet();
}



