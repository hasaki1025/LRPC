package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcURL;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
public class RegisterClient extends Client{

    @Autowired
    RpcRegister rpcRegister;


    @Value("${RPC.Config.HeartCheckTime}")
    int heartCheckTime;
    @Autowired
    DokiDokiMap dokiDokiMap;



    //TODO 并发修改HashMap集合可能会导致的问题
    @Override
    void init() {
        super.init();
        dokiDokiMap.addAllUrl(rpcRegister.getAllUrl());
        workerGroup.scheduleWithFixedDelay(()->{
            for (RpcURL url : rpcRegister.getAllUrl()) {
                dokiDokiMap.checkUrlIsExpire(url);
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }

    public RegisterClient(ChannelHandler[] handlersChain, String host, int port, Class<? extends Channel> channelClass) {
        super(handlersChain, host, port, channelClass);
    }
}
