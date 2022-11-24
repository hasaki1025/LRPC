package com.rpc.lrpc.message.Content.Response;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

import java.util.*;

@Data
public class PullServicesResponse implements ResponseContent {

    Exception exception;
    /**
     * key为serviceName,value是mapping数组
     */
    Map<String,String[]> mappingMap=new HashMap<>();
    Map<String,RpcAddress[]> addressMap=new HashMap<>();



    @Override
    public boolean hasException() {
        return exception!=null;
    }




}
