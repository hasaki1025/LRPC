package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Override
    protected void serverChannelInit() {
        enableDokiDokiCheck();
    }

    private void enableDokiDokiCheck() {
        workerGroup.scheduleAtFixedRate(()->{
            List<RpcAddress> addressNeedRemove = new ArrayList<>();
            for (RpcAddress address : register.getAllAddress()) {
                //检查后DokiDokiMap移除该地址
                if (!dokiDokiMap.checkUrlIsExpire(address)) {
                    addressNeedRemove.add(address);
                }
            }
            for (RpcAddress address : addressNeedRemove) {
                register.removeAddress(address);
            }

        },0,heartCheckTime, TimeUnit.SECONDS);
    }

}
