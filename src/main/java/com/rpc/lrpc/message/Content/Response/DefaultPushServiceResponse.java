package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class DefaultPushServiceResponse implements PushServiceResponse {
    Exception e;
    @Override
    public boolean hasException() {
        return e!=null;
    }

    @Override
    public Exception getException() {
        return e;
    }
}
