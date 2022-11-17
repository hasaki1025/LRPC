package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

@Data
public class DefaultUpdateServiceResponse implements UpdateServiceResponse {

    Exception e;

    RpcService rpcService;
    RpcURL[] rpcURLS;
    @Override
    public boolean hasException() {
        return e!=null;
    }

    @Override
    public Exception getException() {
        return e;
    }

    @Override
    public RpcURL[] getService() {
        return rpcURLS;
    }

    @Override
    public RpcService getServiceName() {
        return rpcService;
    }
}
