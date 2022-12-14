package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class RegisterResponse implements ResponseContent {

    Exception exception;

    @Override
    public boolean hasException() {
        return exception!=null;
    }

    @Override
    public Exception getException() {
        return exception;
    }
}
