package com.rpc.lrpc.net;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;


public class ConsumerClient extends Client {

    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, RpcChannelInitializer channelInitializer, ResponseMap responseMap) {
        super(group, workerGroup, channelInitializer, responseMap);
    }



}
