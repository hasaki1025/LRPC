package com.rpc.lrpc.net;


import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Getter
public class Client implements Closeable {


    final EventLoopGroup group;

    final DefaultEventLoopGroup workerGroup;
    Channel channel;
    final RpcClientChannelInitializer channelInitializer;

    long timeout;


    public Client(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers,long timeout) {
        this.group = group;
        this.workerGroup = workerGroup;
        this.channelInitializer = new RpcClientChannelInitializer(handlers);
        this.timeout=timeout;
    }

    void init(String host, int port, Class<? extends Channel> channelClass) {


        try {
            channel = new Bootstrap()
                    .group(group)
                    .channel(channelClass)
                    .handler(channelInitializer).connect(host, port).sync().channel();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            log.info("connect close.....");
        });
    }


    /**
     * 发送消息的基本方法,如果action中含有consumer则采用异步执行
     * @param message 消息
     */
    protected int sendMessageAsyn(RequestMessage<? extends RequestContent> message,ResponseAction action)
    {
        AtomicInteger seqCounter = (AtomicInteger) channel.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).get();
        //在这里已经设置了SEQ，之后无需再次设置
        int seq = seqCounter.getAndIncrement();
        message.setSeq(seq);
        ChannelResponse responseMap = (ChannelResponse) channel.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
        responseMap.addWaitRequest(seq,action);
        channel.writeAndFlush(message);
        return seq;
    }

    /**
     * 同步发送消息
     * @param message 消息
     * @return 返回一个future用来表示请求的结果
     */
    protected Future<ResponseContent> sendMessageSync(RequestMessage<? extends RequestContent> message) {
        //同步发送
        int seq = sendMessageAsyn(message,new CallResponseAction<>());
        ChannelResponse responseMap = (ChannelResponse) channel.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
        return workerGroup.submit(() -> responseMap.lockAndGetResponse(seq, timeout));
    }






}
