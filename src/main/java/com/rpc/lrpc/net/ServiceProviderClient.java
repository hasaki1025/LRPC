package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.RequestMessage;
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

    void init() {
        super.init(serviceProvider.getRegisterServerHost(),serviceProvider.getRegisterServerPort());
        workerGroup.scheduleWithFixedDelay(()->{
            DokiDokiRequest content = new DokiDokiRequest();
            content.setRpcURL(serviceProvider.getRpcUrl());
            channel.writeAndFlush(new RequestMessage<>(CommandType.DokiDoki, this.getNextSeq(), MessageType.request, content));
        },0,heartGap, TimeUnit.SECONDS);
    }

}
