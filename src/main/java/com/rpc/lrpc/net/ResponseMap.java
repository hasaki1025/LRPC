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
    private final Set<Integer> waitingRequestSet=new CopyOnWriteArraySet<>();

    public boolean stillWaiting(int seq)
    {
        return waitingRequestSet.contains(seq);
    }

    public boolean addWaitingRequest(int seq)
    {
        return waitingRequestSet.add(seq);
    }

    public boolean removeWaitingRequest(int seq)
    {
        return waitingRequestSet.remove(seq);
    }


    public void putResponse(int seq,ResponseContent content)
    {
        responseMap.put(seq, content);
        removeWaitingRequest(seq);
    }

    public ResponseContent getResponse(int seq)
    {
        if (!stillWaiting(seq))
        {
            return responseMap.get(seq);
        }
        return null;
    }



}
