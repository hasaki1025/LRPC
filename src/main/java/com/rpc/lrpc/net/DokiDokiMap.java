package com.rpc.lrpc.net;

import com.rpc.lrpc.message.RpcAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
@Component
public class DokiDokiMap {

    private final ConcurrentHashMap<RpcAddress, Long> dokodoki=new ConcurrentHashMap<>();


    @Value("${RPC.Config.ExpireTime}")
    long expireTime;

    public boolean checkUrlIsExpire(RpcAddress url)
    {
        long lastDokiTime = dokodoki.get(url);
        if (System.currentTimeMillis()-lastDokiTime>expireTime*1000)
        {
            dokodoki.remove(url);
            return false;
        }
        return true;
    }

    public void updateOrAddLastDokiTime(RpcAddress url)
    {
        if (dokodoki.containsKey(url)) {
            dokodoki.put(url,System.currentTimeMillis());
        }

    }

    public void addAllUrl(RpcAddress[] urls)
    {
        for (RpcAddress url : urls) {
            this.addUrl(url);
        }
    }

    public void addUrl(RpcAddress url)
    {
        dokodoki.putIfAbsent(url,System.currentTimeMillis());
    }

}
