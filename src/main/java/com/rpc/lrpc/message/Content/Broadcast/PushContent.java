package com.rpc.lrpc.message.Content.Broadcast;

import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcService;
import lombok.Data;

@Data
public class PushContent implements BroadMassageContent {
    RpcService rpcService;
    RpcAddress rpcAddress;



}
