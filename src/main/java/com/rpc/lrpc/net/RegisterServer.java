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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Server的注册中心实现
 */
@Slf4j
@Component
@ConditionalOnBean(RpcRegister.class)
public class RegisterServer extends Server{

    @Value("${RPC.Register.port}")
    int port;
    private volatile boolean isInit=false;
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

    /**
     * 执行初始化方法，主要是RpcRegisterServerChannelInitializer配置handler
     */
    public void init()
    {
        if (!isInit)
        {
            isInit=true;
            rpcRegisterServerChannelInitializer=new RpcRegisterServerChannelInitializer(channelHandlers,dokiDokiMap,workerGroup,heartCheckTime,register);
            super.init(port,rpcRegisterServerChannelInitializer);
        }
    }

    /**
     * 执行server初始化方法
     */
    @Override
    protected void serverChannelInit() {
        enableDokiDokiCheck();
    }

    /**
     * 心跳检测开启
     */
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
