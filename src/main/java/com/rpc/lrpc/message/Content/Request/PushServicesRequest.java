package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface PushServicesRequest extends RequestContent{

    Map<RpcService,RpcURL[]> getServicesMap();
}
