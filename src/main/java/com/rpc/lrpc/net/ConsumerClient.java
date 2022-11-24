package com.rpc.lrpc.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;


public class ConsumerClient extends Client {

    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, ChannelInitializer<? extends Channel> channelInitializer) {
        super(group, workerGroup, channelInitializer);
    }

    public void sendMessage()
    {
        channel.writeAndFlush("123");
    }
}
