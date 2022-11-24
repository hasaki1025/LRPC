package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java.util.function.Consumer;


public class ConsumerToProviderClient extends Client {


    public ConsumerToProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, long timeout) {
        super(group, workerGroup, handlers, timeout);
    }
    public Optional<Object> callSync(Object[] params, String mapping) throws ExecutionException, InterruptedException {
        CallServicesRequest request = new CallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message =
                new RequestMessage<>(CommandType.Call, MessageType.request, request);
        CallServicesResponse content = (CallServicesResponse) sendMessage(message).get();
        return content.hasException() ? Optional.empty(): Optional.of(content.getResult());
    }

    public<T> void call(Object[] params, String mapping, Consumer<T> consumer)
    {
        CallServicesRequest request = new CallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message = new RequestMessage<>(CommandType.Call, MessageType.request, request);
        sendMessage(message).addListener(future -> {
            CallServicesResponse content = (CallServicesResponse) future.get();
            consumer.accept((T) content.getResult());
        });
    }

}
