package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.message.Content.Request.DefaultRegisterRequest;
import com.rpc.lrpc.message.RpcURL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
@Slf4j
public class Client implements Closeable {
    Channel channel;
    NioEventLoopGroup group;
    DefaultEventLoopGroup workerGroup;

    ChannelHandler[] handlersChain;

    String host;
    int port;
    Class<? extends Channel> channelClass;

    public Client(ChannelHandler[] handlersChain, String host, int port, Class<? extends Channel> channelClass) {
        this.handlersChain = handlersChain;
        this.host = host;
        this.port = port;
        this.channelClass = channelClass;
    }

    void init()
    {
        group=new NioEventLoopGroup();
        workerGroup=new DefaultEventLoopGroup();
        ChannelFuture connect = new Bootstrap()
                .group(group)
                .channel(channelClass)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //TODO 定长协议handler和日志打印handler等
                        pipeline.addLast(handlersChain);
                    }
                }).connect(host, port);
        connect.addListener((ChannelFutureListener) future -> {
            channel= future.channel();
            log.info("Server: {}",channel.remoteAddress());
            log.info("connect successfully....");
        });
    }

    @Override
    public void close() throws IOException {
        SocketAddress remoteAddress = channel.remoteAddress();
        ChannelFuture close = channel.close();
        close.addListener((ChannelFutureListener) future -> {
            log.info("disconnnect...{}", remoteAddress);
            group.shutdownGracefully();
            workerGroup.shutdownGracefully();
        });
    }




}
