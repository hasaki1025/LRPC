package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@ConditionalOnBean(RPCServiceProvider.class)
@Component
@Slf4j
public class ProviderClient extends Client {
    private boolean isInit=false;


    @Autowired
    public ProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers,@Value("${RPC.Config.RequestTimeOut}") long requestTimeOut) {
        super(group, workerGroup, handlers, requestTimeOut);
    }
    @Autowired
    RPCServiceProvider provider;

    public void init() {
        if (!isInit)
        {
            isInit=true;
            super.init(provider.getRegisterServerHost(), provider.getRegisterServerPort(), ChannelType.ToChannelClass(provider.getChannelType()));
            register();
            workerGroup.scheduleAtFixedRate(()->{
                DokiDokiRequest request = new DokiDokiRequest();
                request.setRpcAddress(provider.getRpcUrl());
                channel.writeAndFlush(new RequestMessage<>(
                        CommandType.DokiDoki, MessageType.request, request
                ));
            },0,provider.getHeartGap(), TimeUnit.SECONDS);
        }
    }


    void register()
    {
        RegisterRequest request = new RegisterRequest();
        request.setRpcService(provider.getRpcService());
        request.setRpcAddress(provider.getRpcUrl());
        //TODO 当发送了注册请求后，注册中心并没有那么快收到，但是服务消费者在此之前发送了一个PULL服务列表的请求，并发起了一个Call服务请求,但是服务消费者并没有保存该服务的地址
        sendMessage(new RequestMessage<RegisterRequest>(CommandType.Register, MessageType.request, request))
                .addListener(future -> log.info("register successfullty..."));
    }

}
