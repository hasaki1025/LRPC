package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;

public interface RegisterRequest extends RequestContent {

    RpcService getRpcService();
}
