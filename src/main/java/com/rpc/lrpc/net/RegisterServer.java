package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultDeleteServiceRequest;
import com.rpc.lrpc.message.Content.Request.DeleteServiceRequest;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

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
            for (RpcAddress url : register.getAllUrl()) {
                if (!dokiDokiMap.checkUrlIsExpire(url)) {
                    register.removeAddress(url);
                    if (url.getServiceName()!=null && !"".equals(url.getServiceName()))
                    {
                        DefaultDeleteServiceRequest request = new DefaultDeleteServiceRequest(url);
                        Server.broadcastMessage(new RequestMessage<DeleteServiceRequest>(CommandType.Delete, MessageType.request,request));
                    }
                }
            }
        },0,heartCheckTime, TimeUnit.SECONDS);
    }

}
