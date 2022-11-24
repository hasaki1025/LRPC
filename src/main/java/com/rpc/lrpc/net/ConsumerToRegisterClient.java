package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@ConditionalOnBean(RpcConsumer.class)
@Slf4j
public class ConsumerToRegisterClient extends Client{

    private boolean isInit=false;
    @Autowired
    RpcConsumer rpcConsumer;
    @Autowired
    LoggingHandler loggingHandler;

    public void init() throws ExecutionException, InterruptedException {
        if (!isInit)
        {
            isInit=true;
            super.init(rpcConsumer.getRegisterServerHost(), rpcConsumer.getRegisterServerPort(), ChannelType.ToChannelClass(rpcConsumer.getChannelType()));
            pull();
        }
    }

    @Autowired
    public ConsumerToRegisterClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, @Value("${RPC.Config.RequestTimeOut}") long timeout) {
        super(group, workerGroup, handlers, timeout);
    }


    public void pull() throws ExecutionException, InterruptedException {
        PullServicesRequest request = new PullServicesRequest();
        RequestMessage<PullServicesRequest> message = new RequestMessage<>(CommandType.Pull, MessageType.request, request);
        sendMessage(message).get();
    }








}
