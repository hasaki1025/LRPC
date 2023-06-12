package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnBean(RPCServiceProvider.class)
@Component
public class ServiceProviderServer extends Server{

    @Value("${RPC.Provider.port}")
    int port;

    private volatile boolean isInit=false;


    @Autowired
    List<ChannelHandler> channelHandlers;





    public void init() {
        if (!isInit)
        {
            isInit=true;
            super.init(port,new RpcProviderServerChannelInitializer(channelHandlers));
        }

    }
}
