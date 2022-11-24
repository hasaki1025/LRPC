package com.rpc.lrpc.net;

import com.rpc.lrpc.message.RpcURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class DokiDokiMap {

    private final ConcurrentHashMap<RpcURL, Long> dokodoki=new ConcurrentHashMap<>();


    @Value("${RPC.Config.ExpireTime}")
    long expireTime;

    public boolean checkUrlIsExpire(RpcURL url)
    {
        long lastDokiTime = dokodoki.get(url);
        if (System.currentTimeMillis()-lastDokiTime>expireTime*1000)
        {
            dokodoki.remove(url);
            return false;
        }
        return true;
    }

    public void updateOrAddLastDokiTime(RpcURL url)
    {
        dokodoki.put(url,System.currentTimeMillis());
    }

    public void addAllUrl(RpcURL[] urls)
    {
        for (RpcURL url : urls) {
            dokodoki.put(url,System.currentTimeMillis());
        }
    }

    public void addUrl(RpcURL url)
    {
        dokodoki.put(url,System.currentTimeMillis());
    }

}
