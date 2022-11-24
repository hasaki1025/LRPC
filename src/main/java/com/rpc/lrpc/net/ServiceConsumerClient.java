package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcConsumerContext;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.CallServicesRequest;
import com.rpc.lrpc.message.Content.Request.DefaultCallServicesRequest;
import com.rpc.lrpc.message.Content.Request.DefaultPullServicesRequest;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RpcConsumer.class)
@Slf4j
public class ServiceConsumerClient extends Client{


    @Value("${RPC.Config.RequestTimeOut}")
    long requestTimeOut;
    @Autowired
    RpcConsumer rpcConsumer;


    @Override
    void channelInit() {
        pullServices();
    }

    private void pullServices()
    {
        channel.writeAndFlush(new RequestMessage<PullServicesRequest>(CommandType.Pull, getNextSeq(), MessageType.request, new DefaultPullServicesRequest()));
    }


    public <T> T callService(Object[] params,Class<T> returnType,String mapping,String serviceName)
    {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);

    }
}
