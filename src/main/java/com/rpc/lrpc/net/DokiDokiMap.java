package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 心跳发送Map
 */
@Component
@ConditionalOnBean(RpcRegister.class)
public class DokiDokiMap {

    /**
     * 记录上次收到从指定URL心跳信息的时间
     */
    private final ConcurrentHashMap<RpcAddress, Long> dokodoki=new ConcurrentHashMap<>();


    @Value("${RPC.Config.ExpireTime}")
    long expireTime;

    /**
     * 定时检测该服务是否过期
     * @param url 服务URL
     * @return 是否过期
     */
    public boolean checkUrlIsExpire(RpcAddress url)
    {
        if (!dokodoki.containsKey(url))
        {
            dokodoki.put(url,System.currentTimeMillis());
            return true;
        }
        long lastDokiTime = dokodoki.get(url);
        if (System.currentTimeMillis()-lastDokiTime>expireTime*1000)
        {
            dokodoki.remove(url);
            return false;
        }
        return true;
    }

    /**
     * 更新指定URL最后一次接收到心跳的时间
     * @param url 接受到来自该url的心跳
     */
    public void updateOrAddLastDokiTime(RpcAddress url)
    {
        if (dokodoki.containsKey(url)) {
            dokodoki.put(url,System.currentTimeMillis());
        }

    }

    /**
     * 批量新增心跳检测url
     * @param urls url
     */
    public void addAllUrl(RpcAddress[] urls)
    {
        for (RpcAddress url : urls) {
            this.addUrl(url);
        }
    }

    /**
     * 新增心跳检测url
     * @param url 新增的URL
     */
    public void addUrl(RpcAddress url)
    {
        dokodoki.putIfAbsent(url,System.currentTimeMillis());
    }

}
