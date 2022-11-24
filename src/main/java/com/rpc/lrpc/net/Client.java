package com.rpc.lrpc.net;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;


@Slf4j
@Getter
public class Client implements Closeable {


    final EventLoopGroup group;

    final DefaultEventLoopGroup workerGroup;
    Channel channel;
    final RpcClientChannelInitializer channelInitializer;


     final ResponseMap responseMap;

    public Client(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, ResponseMap responseMap) {
        this.group = group;
        this.workerGroup = workerGroup;
        this.channelInitializer = new RpcClientChannelInitializer(handlers);
        this.responseMap=responseMap;
    }

    void init(String host, int port, Class<? extends Channel> channelClass)
    {

        try {
            channel = new Bootstrap()
                .group(group)
                .channel(channelClass)
                .handler(channelInitializer).connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            log.info("connect close.....");
        });
    }




}
