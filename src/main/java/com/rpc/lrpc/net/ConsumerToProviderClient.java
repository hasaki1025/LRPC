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

/**
 * 消费者对服务提供者的Client
 */
public class ConsumerToProviderClient extends Client {


    public ConsumerToProviderClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, long timeout) {
        super(group, workerGroup, handlers, timeout);
    }

    /**
     * 同步调用
     * @param params 参数
     * @param mapping 映射
     * @return 一个Optional防止空指针异常
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 中断异常防止无限执行
     */
    public Optional<Object> callSync(Object[] params, String mapping) throws ExecutionException, InterruptedException {
        CallServicesRequest request = new CallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message =
                new RequestMessage<>(CommandType.Call, MessageType.request, request);
        CallServicesResponse content = (CallServicesResponse) sendMessageSync(message).get();
        return content.hasException() ? Optional.empty(): Optional.of(content.getResult());
    }

    /**
     * 异步调用
     * @param params 调用参数
     * @param mapping 调用mapping
     * @param consumer 对响应数据的操作
     * @param <T> 响应数据类型
     */
    public<T> void callAsyn(Object[] params, String mapping, Consumer<T> consumer)
    {
        CallServicesRequest request = new CallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message = new RequestMessage<>(CommandType.Call, MessageType.request, request);
        CallResponseAction<T> action = new CallResponseAction<>();
        action.setConsumer(consumer);
        sendMessageAsyn(message,action);
    }

}
