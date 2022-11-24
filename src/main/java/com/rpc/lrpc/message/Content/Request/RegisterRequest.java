package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RegisterRequest implements RequestContent{

    String[] mappings;

    RpcAddress rpcAddress;

    public void setRpcService(RpcService rpcService) {
        ArrayList<String> list = new ArrayList<>();
        for (RpcMapping mapping : rpcService.getRpcMappings()) {
            list.add(mapping.getMapping());
        }
        mappings=list.toArray(new String[0]);
    }
}
