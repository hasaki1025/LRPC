package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class DefaultRegisterResponse implements RegisterResponse {
    boolean isSuccessfully;

    @Override
    public Object getValue() {
        return isSuccessfully;
    }
}
