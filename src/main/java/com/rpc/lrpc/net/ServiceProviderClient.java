package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultRegisterRequest;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnBean(RPCServiceProvider.class)
public class ServiceProviderClient extends Client {


    @Autowired
    RPCServiceProvider serviceProvider;

    @Value("${RPC.Config.HeartGap}")
    int heartGap;



    @Override
     void channelInit() {
        //注册服务
        registerService();
        //启动心跳发送
        workerGroup.scheduleWithFixedDelay(this::sendDokiDoki,0,heartGap, TimeUnit.SECONDS);
    }

    private void sendDokiDoki() {
        DokiDokiRequest content = new DokiDokiRequest();
        content.setRpcURL(serviceProvider.getRpcUrl());
        channel.writeAndFlush(new RequestMessage<>(CommandType.DokiDoki, this.getNextSeq(), MessageType.request, content));
    }

    public void registerService()
    {
        DefaultRegisterRequest request = new DefaultRegisterRequest();
        request.setRpcURL(serviceProvider.getRpcUrl());
        request.setRpcService(serviceProvider.getRpcService());
        channel.writeAndFlush(new RequestMessage<RegisterRequest>(CommandType.Register, getNextSeq(), MessageType.request, request));
    }






}
