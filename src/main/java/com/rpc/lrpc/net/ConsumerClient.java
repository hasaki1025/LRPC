package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.Content.Response.UpdateServiceResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;
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

    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, ResponseMap responseMap) {
        super(group, workerGroup, handlers, responseMap);
    }


    public void pull()
    {
        DefaultPullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeAndFlush(new RequestMessage<PullServicesRequest>(CommandType.Pull, MessageType.request, request));
    }

    public RpcAddress update(String rpcServiceName)
    {
        Future<?> submit = workerGroup.submit(() -> updateSync(rpcServiceName));
        submit.addListener(future -> {
            if (future.isSuccess())
            {
                RpcAddress address = (RpcAddress) future.get();
                //TODO 返回或者调用Call
            }
        });
    }

    public RpcAddress updateSync(String rpcServiceName)
    {
        DefaultUpdateServiceRequest request = new DefaultUpdateServiceRequest();
        request.setServiceName(rpcServiceName);
        RequestMessage<UpdateServiceRequest> message =
                new RequestMessage<>(CommandType.Update, MessageType.request, request);
        UpdateServiceResponse content = (UpdateServiceResponse) sendMessageSync(message);
        if (!content.hasException()) {
            RpcAddress[] addresses = content.getRpcAddresses();
            if (addresses.length>0) {
                return addresses[0];
            }
        }
        return null;
    }

    private ResponseContent sendMessageSync(RequestMessage<? extends RequestContent> message) {
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

    public Object callSync(Object[] params, String mapping) {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setParamValues(params);
        request.setMapping(mapping);
        RequestMessage<CallServicesRequest> message =
                new RequestMessage<>(CommandType.Call, MessageType.request, request);
        CallServicesResponse content = (CallServicesResponse) sendMessageSync(message);
        if (!content.hasException()) {
            return content.getResult();
        }
        return null;
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
                CallServicesResponse response = (CallServicesResponse) responseMap.getResponse(seq);
                if (!response.hasException())
                {
                    consumer.accept((T)response.getResult());
                }
            }
        });
    }
}
