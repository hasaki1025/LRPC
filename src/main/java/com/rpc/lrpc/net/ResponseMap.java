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
    public static final Map<Integer,Object> WAITING_MAP =new ConcurrentHashMap<>();


    public boolean stillWaiting(int seq)
    {
        return WAITING_MAP.containsKey(seq);
    }

    public void addWaitingRequest(int seq)
    {
        WAITING_MAP.put(seq, new Object());
    }

    public void removeWaitingRequest(int seq)
    {
        synchronized (WAITING_MAP.get(seq))
        {
            try {
                WAITING_MAP.get(seq).notifyAll();
                WAITING_MAP.remove(seq);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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
