package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class CallServicesResponse implements ResponseContent{

    Exception exception;

    Object result;

   @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public Exception getException() {
        return exception;
    }
}
