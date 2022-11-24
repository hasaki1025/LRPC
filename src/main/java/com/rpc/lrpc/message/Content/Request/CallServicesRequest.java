package com.rpc.lrpc.message.Content.Request;

import lombok.Data;

@Data
public class CallServicesRequest implements RequestContent{

    String mapping;
    Object[] paramValues;

}
