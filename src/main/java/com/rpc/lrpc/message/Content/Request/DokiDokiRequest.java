package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

@Data
public class DokiDokiRequest implements RequestContent {
    RpcURL rpcURL;
}
