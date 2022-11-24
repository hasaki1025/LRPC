package com.rpc.lrpc.message.Content.Response;



import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;


public interface PullServicesResponse extends ResponseContent{

    Map<RpcService,RpcURL[]> getRpcServiceMap();
    void addRpcService(Map<RpcService,RpcURL[]> map);
}



