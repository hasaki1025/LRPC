package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.ResponseContent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelResponse {
    private final Map<Integer,Object> waitingMap=new ConcurrentHashMap<>();
    private final Map<Integer, ResponseContent> resultMap=new ConcurrentHashMap<>();

    public ResponseContent lockAndGetResponse(int seq, long waitingTime)  {
        try
        {
            waitingMap.put(seq,new Object());
            synchronized (waitingMap.get(seq)) {
                waitingMap.get(seq).wait(waitingTime);
                return resultMap.get(seq);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public void addWaitRequest(int seq)
    {
        waitingMap.putIfAbsent(seq,new Object());
    }
    public boolean stillWaiting(int seq)
    {
        return waitingMap.containsKey(seq);
    }
    public ResponseContent getResponse(int seq)
    {
        ResponseContent content = resultMap.get(seq);
        resultMap.remove(seq);
        return content;
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
    }

}
