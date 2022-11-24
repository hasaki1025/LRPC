package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcURL;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnBean(RpcRegister.class)
public class RegsiterServer implements Closeable {
    @Value("${RPC.Register.port}")
    int port;
    NioEventLoopGroup bossGroup;
    NioEventLoopGroup chirdGroup;
    DefaultEventLoopGroup workerGrop;
    NioServerSocketChannel channel;
    @Autowired
    List<ChannelHandler> handlersChain;
    @Autowired
    ResponseMap responseMap;
    @Autowired
    RpcRegister register;
    @Autowired
    DokiDokiMap dokiDokiMap;
    @Value("${RPC.Config.HeartCheckTime}")
    int heartCheckTime;


    void init()
    {
        bossGroup=new NioEventLoopGroup(1);
        chirdGroup=new NioEventLoopGroup();
        workerGrop=new DefaultEventLoopGroup();
        new ServerBootstrap()
                .group(bossGroup,chirdGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                    }
                }).bind(port).addListener((ChannelFutureListener) future -> {
                    Channel channel1 = future.channel();
                    if (channel1 instanceof NioServerSocketChannel)
                        channel= (NioServerSocketChannel) channel1;
                });


        dokiDokiMap.addAllUrl(register.getAllUrl());
        workerGrop.scheduleWithFixedDelay(()->{
            for (RpcURL url : register.getAllUrl()) {
                dokiDokiMap.checkUrlIsExpire(url);
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }

    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            workerGrop.shutdownGracefully();
            chirdGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        });
    }
}
