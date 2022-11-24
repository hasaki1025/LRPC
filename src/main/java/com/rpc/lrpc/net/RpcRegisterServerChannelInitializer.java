package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;

import java.util.List;


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



}
