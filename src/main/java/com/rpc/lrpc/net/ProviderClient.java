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
        //TODO ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????PULL??????????????????????????????????????????Call????????????,??????????????????????????????????????????????????????
        sendMessage(new RequestMessage<RegisterRequest>(CommandType.Register, MessageType.request, request))
                .addListener(future -> log.info("register successfullty..."));
    }

}
