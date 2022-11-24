package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {



    DokiDokiMap dokiDokiMap;

    DefaultEventLoopGroup workerGroup;


    int heartCheckTime;

    RpcRegister register;

    public RpcRegisterServerChannelInitializer(List<ChannelHandler> handlersChain, DokiDokiMap dokiDokiMap, DefaultEventLoopGroup workerGroup, int heartCheckTime, RpcRegister register) {
        super(handlersChain);
        this.dokiDokiMap = dokiDokiMap;
        this.workerGroup = workerGroup;
        this.heartCheckTime = heartCheckTime;
        this.register = register;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        dokiDokiMap.addAllUrl(register.getAllUrl());
        workerGroup.scheduleAtFixedRate(()->{
            for (RpcAddress url : register.getAllUrl()) {
                if (!dokiDokiMap.checkUrlIsExpire(url)) {
                    register.removeAddress(url);
                }
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }
}
