package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

public interface RegisterRequest extends RequestContent {

    RpcService getRpcService();

    RpcURL getRpcURL();
}
