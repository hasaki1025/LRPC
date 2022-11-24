package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcURL;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
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
public class RegsiterServer  extends Server{

    @Autowired
    ResponseMap responseMap;
    @Autowired
    RpcRegister register;
    @Autowired
    DokiDokiMap dokiDokiMap;
    @Value("${RPC.Config.HeartCheckTime}")
    int heartCheckTime;

    @Override
    void NIOServerInit() {
        dokiDokiMap.addAllUrl(register.getAllUrl());
        workerGrop.scheduleWithFixedDelay(()->{
            for (RpcURL url : register.getAllUrl()) {
                dokiDokiMap.checkUrlIsExpire(url);
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }

}
