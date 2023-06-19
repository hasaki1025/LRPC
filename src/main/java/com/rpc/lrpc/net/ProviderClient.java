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

/**
 * 服务提供者对注册中心的Client
 */
@ConditionalOnBean(RPCServiceProvider.class)
@Component
@Slf4j
public class ProviderClient extends Client {
    /**
     * 是否已初始化
     */
    private volatile boolean isInit=false;


    @Autowired
    public ProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers,@Value("${RPC.Config.RequestTimeOut}") long requestTimeOut) {
        super(group, workerGroup, handlers, requestTimeOut);
    }
    @Autowired
    RPCServiceProvider provider;


    /**
     * 初始化client，主要启动心跳发送任务和执行注册任务
     */
    public void init() {
        if (!isInit)
        {
            isInit=true;
            super.init(provider.getRegisterServerHost(), provider.getRegisterServerPort(), ChannelType.ToChannelClass(provider.getChannelType()));
            //注册
            register();
            //启动一个定时任务定期执行心跳发送
            workerGroup.scheduleAtFixedRate(()->{
                DokiDokiRequest request = new DokiDokiRequest();
                request.setRpcAddress(provider.getRpcUrl());
                channel.writeAndFlush(new RequestMessage<>(
                        CommandType.DokiDoki, MessageType.request, request
                ));
            },0,provider.getHeartGap(), TimeUnit.SECONDS);
        }
    }


    /**
     * 注册本地服务
     */
    void register()
    {
        RegisterRequest request = new RegisterRequest();
        request.setRpcService(provider.getRpcService());
        request.setRpcAddress(provider.getRpcUrl());
        //TODO 当发送了注册请求后，注册中心并没有那么快收到，但是服务消费者在此之前发送了一个PULL服务列表的请求，并发起了一个Call服务请求,但是服务消费者并没有保存该服务的地址
        sendMessageAsyn(new RequestMessage<>(CommandType.Register, MessageType.request, request),new ResponseAction(){
            @Override
            public void action() {
                log.info("register successfullty...");
            }
        });
    }

}
