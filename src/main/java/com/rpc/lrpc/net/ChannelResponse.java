package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.ResponseContent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChannelResponse {
    private final Map<Integer,Object> waitingMap=new ConcurrentHashMap<>();
    private final Map<Integer, ResponseContent> resultMap=new ConcurrentHashMap<>();

    public void lock(int seq,long waitingTime) throws InterruptedException {
        synchronized (waitingMap.get(seq)) {
            waitingMap.get(seq).wait(waitingTime);
        }
    }
    public void addWaitRequest(int seq)
    {
        waitingMap.put(seq,new Object());
    }
    public boolean stillWaiting(int seq)
    {
        return waitingMap.containsKey(seq);
    }
    public ResponseContent getResponse(int seq)
    {
        return resultMap.get(seq);
    }

    public void putResponse(int seq,ResponseContent content)
    {
        resultMap.put(seq,content);
        synchronized (waitingMap.get(seq)) {
            waitingMap.get(seq).notifyAll();
            waitingMap.remove(seq);
        }
    }

    public void removeWaitingRequest(int seq)
    {
        waitingMap.remove(seq);
        resultMap.remove(seq);
    }

}
