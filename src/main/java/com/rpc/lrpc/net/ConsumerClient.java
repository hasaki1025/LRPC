package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class ConsumerClient extends Client {


    private boolean isInit=false;

    @Override
    void init(String host, int port, Class<? extends Channel> channelClass) {
        if (!isInit)
        {
            isInit=true;
            super.init(host, port, channelClass);
        }
    }


    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers,long timeout) {
        super(group, workerGroup, handlers, timeout);
    }


    public void pull() throws ExecutionException, InterruptedException {
        DefaultPullServicesRequest request = new DefaultPullServicesRequest();
        request.setObject(new Object());
        RequestMessage<PullServicesRequest> message = new RequestMessage<>(CommandType.Pull, MessageType.request, request);
        sendMessage(message).get();
    }




    public Optional<Object> callSync(Object[] params, String mapping) throws ExecutionException, InterruptedException {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message =
                new RequestMessage<>(CommandType.Call, MessageType.request, request);
        CallServicesResponse content = (CallServicesResponse) sendMessage(message).get();
        return content.hasException() ? Optional.empty(): Optional.of(content.getResult());
    }

    public<T> void call(Object[] params, String mapping, Consumer<T> consumer)
    {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message = new RequestMessage<>(CommandType.Call, MessageType.request, request);
        sendMessage(message).addListener(future -> {
            DefaultCallServicesResponse content = (DefaultCallServicesResponse) future.get();
            consumer.accept((T) content.getResult());
        });
    }
}
