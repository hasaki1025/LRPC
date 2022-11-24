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
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Getter
public class Client implements Closeable {


    final EventLoopGroup group;

    final DefaultEventLoopGroup workerGroup;
    Channel channel;
    final RpcClientChannelInitializer channelInitializer;

    ChannelResponse channelResponse;
    long timeout;


    public Client(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers,long timeout) {
        this.group = group;
        this.workerGroup = workerGroup;
        channelResponse=new ChannelResponse();
        this.channelInitializer = new RpcClientChannelInitializer(handlers,channelResponse);
        this.timeout=timeout;
    }

    void init(String host, int port, Class<? extends Channel> channelClass)
    {
        try {
            channel = new Bootstrap()
                    .group(group)
                    .channel(channelClass)
                    .handler(channelInitializer).connect(host, port).sync().channel();
            channelInit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void channelInit()
    {
        //NOOP
    }


    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            log.info("connect close.....");
        });
    }

    protected Future<ResponseContent> sendMessage(RequestMessage<? extends RequestContent> message) {
        AtomicInteger seqCounter = (AtomicInteger) channel.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).get();
        //在这里已经设置了SEQ，之后无需再次设置
        int seq = seqCounter.getAndIncrement();
        message.setSeq(seq);

        channel.writeAndFlush(message);

        return workerGroup.submit(() -> {
            try {
                channelResponse.lock(seq,timeout);
                return channelResponse.getResponse(seq);
            } catch (InterruptedException e) {
                channelResponse.removeWaitingRequest(seq);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }




}
