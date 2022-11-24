package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java.util.function.Consumer;


public class ConsumerToProviderClient extends Client {


    public ConsumerToProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, long timeout) {
        super(group, workerGroup, handlers, timeout);
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
