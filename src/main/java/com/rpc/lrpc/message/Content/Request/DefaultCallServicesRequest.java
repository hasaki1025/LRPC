package com.rpc.lrpc.message.Content.Request;

import lombok.Data;

@Data
public class DefaultCallServicesRequest implements CallServicesRequest{

    String mapping;
    Object[] paramValues;

}