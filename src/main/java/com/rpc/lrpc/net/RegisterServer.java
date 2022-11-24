package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ConditionalOnBean(RpcRegister.class)
public class RegisterServer extends Server{

    @Value("${RPC.Register.port}")
    int port;
    private boolean isInit=false;
    @Autowired
    List<ChannelHandler> channelHandlers;
    @Autowired
    DokiDokiMap dokiDokiMap;
    @Autowired
    DefaultEventLoopGroup workerGroup;
    @Value("${RPC.Config.HeartCheckTime}")
    int heartCheckTime;
    @Autowired
    RpcRegister register;
    RpcRegisterServerChannelInitializer rpcRegisterServerChannelInitializer;
    public void init()
    {
        if (!isInit)
        {
            isInit=true;
            rpcRegisterServerChannelInitializer=new RpcRegisterServerChannelInitializer(channelHandlers,dokiDokiMap,workerGroup,heartCheckTime,register);
            super.init(port,rpcRegisterServerChannelInitializer);
        }

    }

}
