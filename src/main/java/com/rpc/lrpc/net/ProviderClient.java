package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultRegisterRequest;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@ConditionalOnBean(RPCServiceProvider.class)
@Component
public class ProviderClient extends Client {
    @Autowired
    public ProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup,  RpcClientChannelInitializer channelInitializer, ResponseMap responseMap) {
        super(group, workerGroup, channelInitializer, responseMap);
    }
    @Autowired
    RPCServiceProvider provider;

    void init() {
        super.init(provider.getRegisterServerHost(), provider.getRegisterServerPort(), ChannelType.ToChannelClass(provider.getChannelType()));
        workerGroup.scheduleAtFixedRate(()->{
            DokiDokiRequest request = new DokiDokiRequest();
            request.setRpcURL(provider.getRpcUrl());
            channel.writeAndFlush(new RequestMessage<>(
                    CommandType.DokiDoki, MessageType.request, request
            ));
        },0,provider.getHeartGap(), TimeUnit.SECONDS);
    }

    void register()
    {
        DefaultRegisterRequest request = new DefaultRegisterRequest();
        request.setRpcService(provider.getRpcService());
        request.setRpcURL(provider.getRpcUrl());
        channel.writeAndFlush(new RequestMessage<RegisterRequest>(CommandType.Register, MessageType.request, request));
    }

}
