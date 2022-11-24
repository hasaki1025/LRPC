package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultDeleteServiceRequest;
import com.rpc.lrpc.message.Content.Request.DeleteServiceRequest;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.ServerChannel;

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



}
