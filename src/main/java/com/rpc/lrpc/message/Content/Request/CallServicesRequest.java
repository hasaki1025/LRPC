package com.rpc.lrpc.message.Content.Request;

public interface CallServicesRequest extends RequestContent {
    String getMapping();
    Object[] getParamValues();
}
