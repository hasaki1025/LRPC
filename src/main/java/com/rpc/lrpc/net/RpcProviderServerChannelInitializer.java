package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import io.netty.channel.ChannelHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;


public class RpcProviderServerChannelInitializer extends RpcServerChannelInitializer {

    List<ChannelHandler> channelHandlers;

    public RpcProviderServerChannelInitializer(List<ChannelHandler> handlersChain) {
        super(handlersChain);
        this.channelHandlers = handlersChain;
    }
}
