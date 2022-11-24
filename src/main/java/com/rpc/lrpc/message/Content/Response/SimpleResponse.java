package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class SimpleResponse implements ResponseContent{
    Exception exception;
    @Override
    public boolean hasException() {
        return exception!=null;
    }
}
