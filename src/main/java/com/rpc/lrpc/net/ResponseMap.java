package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.Content.Response.UpdateServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ResponseMap {

    private final Map<Integer, ResponseContent> responseMap=new ConcurrentHashMap<>();
    public static final Map<Integer,Object> WAITING_MAP =new ConcurrentHashMap<>();

    @Value("${RPC.Config.RequestTimeOut}")
    long requestTimeOut;



    public boolean stillWaiting(int seq)
    {
        return WAITING_MAP.containsKey(seq);
    }


    public void addWaitingRequest(int seq)
    {
        WAITING_MAP.put(seq, new Object());
    }

    public void removeWaitingRequestSync(int seq)
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
    public void removeWaitingRequest(int seq)
    {
       WAITING_MAP.remove(seq);
    }


    public void putResponse(int seq,ResponseContent content)
    {
        responseMap.put(seq, content);
        if (content instanceof CallServicesResponse || content instanceof UpdateServiceResponse)
        {
            removeWaitingRequestSync(seq);
        }
        else {
            removeWaitingRequest(seq);
        }

    }

    public ResponseContent getResponse(int seq)
    {
        if (!stillWaiting(seq))
        {
            responseMap.remove(seq);
            return responseMap.get(seq);
        }
        return null;
    }


    public long getRequestTimeOut() {
        return requestTimeOut;
    }
}
