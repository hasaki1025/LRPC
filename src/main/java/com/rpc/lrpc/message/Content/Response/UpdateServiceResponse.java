package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

public interface UpdateServiceResponse extends ResponseContent{

    RpcAddress[] getRpcUrls();
    RpcService getRpcService();

    void setRpcService(RpcService rpcService);
    void setRpcUrls(RpcAddress[] rpcAddresses);
}
