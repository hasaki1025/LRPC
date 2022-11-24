package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

@Data
public class DokiDokiResponse implements ResponseContent {


    Exception exception;

    RpcAddress rpcAddress;

    @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public Exception getException() {
        return exception;
    }
}
