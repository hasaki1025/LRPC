package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

public interface UpdateServiceResponse extends ResponseContent{

    RpcAddress[] getRpcAddresses();
    RpcService getRpcService();

    void setRpcService(RpcService rpcService);
    void setRpcAddresses(RpcAddress[] rpcAddresses);
}
