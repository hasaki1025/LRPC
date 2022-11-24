package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

@Data
public class DefaultUpdateServiceResponse implements UpdateServiceResponse {

    Exception exception;

    RpcService rpcService;
    RpcAddress[] rpcAddresses;
    @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public Exception getException() {
        return exception;
    }

}
