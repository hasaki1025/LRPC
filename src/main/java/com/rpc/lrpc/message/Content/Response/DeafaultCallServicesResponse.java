package com.rpc.lrpc.message.Content.Response;

import lombok.Data;

@Data
public class DeafaultCallServicesResponse implements CallServicesResponse{
    Object returnValue;

    @Override
    public Object getValue() {
        return returnValue;
    }
}
