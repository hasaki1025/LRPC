package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import io.netty.channel.ChannelHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务提供者信道初始化类
 */
public class RpcProviderServerChannelInitializer extends RpcServerChannelInitializer {

    List<ChannelHandler> channelHandlers;

    /**
     * 初始化信道
     * @param handlersChain handler处理器链
     */
    public RpcProviderServerChannelInitializer(List<ChannelHandler> handlersChain) {
        super(handlersChain);
        this.channelHandlers = handlersChain;
    }
}
