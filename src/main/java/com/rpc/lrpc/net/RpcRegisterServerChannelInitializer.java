package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.message.RpcURL;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnBean(RpcRegister.class)
public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {


    @Autowired
    DokiDokiMap dokiDokiMap;
    @Autowired
    DefaultEventLoopGroup workerGroup;

    @Value("${RPC.Config.HeartCheckTime}")
    int heartCheckTime;

    @Autowired
    RpcRegister register;
    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        dokiDokiMap.addAllUrl(register.getAllUrl());
        workerGroup.scheduleAtFixedRate(()->{
            for (RpcURL url : register.getAllUrl()) {
                dokiDokiMap.checkUrlIsExpire(url);
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }
}
