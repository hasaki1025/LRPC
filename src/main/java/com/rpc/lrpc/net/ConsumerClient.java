package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class ConsumerClient extends Client {

    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, RpcClientChannelInitializer channelInitializer, ResponseMap responseMap) {
        super(group, workerGroup, channelInitializer, responseMap);
    }


    public void pull()
    {
        DefaultPullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeAndFlush(new RequestMessage<PullServicesRequest>(CommandType.Pull, MessageType.request, request));
    }

    public void update(String rpcServiceName)
    {
        DefaultUpdateServiceRequest request = new DefaultUpdateServiceRequest();
        request.setServiceName(rpcServiceName);
        channel.writeAndFlush(new RequestMessage<UpdateServiceRequest>(CommandType.Update, MessageType.request, request));
    }

    public Object callSync(Object[] params, String mapping) {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message = new RequestMessage<>(CommandType.Call, MessageType.request, request);
        AtomicInteger seqCounter = (AtomicInteger) channel.attr(AttributeKey.valueOf("seqCounter")).get();
        //在这里已经设置了SEQ，之后无需再次设置
        int seq = seqCounter.getAndIncrement();
        message.setSeq(seq);
        ResponseMap.WAITING_MAP.put(seq, new Object());
        channel.writeAndFlush(message);
        synchronized (ResponseMap.WAITING_MAP.get(seq)) {
            try {
                ResponseMap.WAITING_MAP.get(seq).wait(responseMap.getRequestTimeOut());
            } catch (InterruptedException e) {
                responseMap.removeWaitingRequest(seq);
                throw new RuntimeException(e);
            }
        }
        return responseMap.getResponse(seq);
    }

    public<T> void call(Object[] params, String mapping, Consumer<T> consumer)
    {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message = new RequestMessage<>(CommandType.Call, MessageType.request, request);
        AtomicInteger seqCounter = (AtomicInteger) channel.attr(AttributeKey.valueOf("seqCounter")).get();
        //在这里已经设置了SEQ，之后无需再次设置
        int seq = seqCounter.getAndIncrement();
        message.setSeq(seq);
        ResponseMap.WAITING_MAP.put(seq, new Object());
        channel.writeAndFlush(message);
        workerGroup.submit(()->{
            synchronized (ResponseMap.WAITING_MAP.get(seq)) {
                try {
                    ResponseMap.WAITING_MAP.get(seq).wait(responseMap.getRequestTimeOut());
                } catch (InterruptedException e) {
                    responseMap.removeWaitingRequest(seq);
                    throw new RuntimeException(e);
                }
                consumer.accept((T)responseMap.getResponse(seq));
            }
        });
    }
}
