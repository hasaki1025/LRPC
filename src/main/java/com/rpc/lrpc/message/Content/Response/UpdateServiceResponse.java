package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

public interface UpdateServiceResponse extends ResponseContent{

    RpcURL[] getRpcUrls();
    RpcService getRpcService();

    void setRpcService(RpcService rpcService);
    void setRpcUrls(RpcURL[] rpcUrls);
}
