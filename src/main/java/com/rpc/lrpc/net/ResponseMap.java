package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.ResponseContent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
public class ResponseMap {

    private final Map<Integer, ResponseContent> responseMap=new ConcurrentHashMap<>();

    public boolean stillWaiting(int seq)
    {
        return responseMap.containsKey(seq);
    }


    public void putResponse(int seq,ResponseContent content)
    {
        responseMap.put(seq, content);
    }

    public ResponseContent getResponse(int seq)
    {
        return responseMap.get(seq);
    }



}
