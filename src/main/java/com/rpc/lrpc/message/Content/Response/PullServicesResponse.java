package com.rpc.lrpc.message.Content.Response;



import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Map;


public interface PullServicesResponse extends ResponseContent{

    Map<RpcService, RpcAddress[]> getRpcServiceMap();
    void addRpcService(Map<RpcService, RpcAddress[]> map);
}



