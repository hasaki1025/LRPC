package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import lombok.Data;

@Data
public class DefaultRegisterRequest implements RegisterRequest{

    RpcService rpcService;
}
