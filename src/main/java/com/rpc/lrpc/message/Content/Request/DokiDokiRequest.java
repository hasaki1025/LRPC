package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

@Data
public class DokiDokiRequest implements RequestContent {
    RpcAddress rpcAddress;
}
