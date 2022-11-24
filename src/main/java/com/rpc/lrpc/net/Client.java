package com.rpc.lrpc.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;


@Slf4j
@Data
public class Client implements Closeable {


    EventLoopGroup group;

    DefaultEventLoopGroup workerGroup;
    Channel channel;
    ChannelInitializer<? extends Channel> channelInitializer;

    public Client(EventLoopGroup group, DefaultEventLoopGroup workerGroup,ChannelInitializer<? extends Channel> channelInitializer) {
        this.group = group;
        this.workerGroup = workerGroup;
        this.channelInitializer = channelInitializer;
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
