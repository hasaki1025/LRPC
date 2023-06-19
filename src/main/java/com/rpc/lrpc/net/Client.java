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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端基本类
 */
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

    /**
     * netty客户端初始化
     * @param host 连接IP地址
     * @param port 连接端口
     * @param channelClass 信道类型
     */
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


    /**
     * 关闭当前连接
     * @throws IOException 连接关闭抛出
     */
    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            log.info("connect close.....");
        });
    }


    /**
     * 发送消息的基本方法,默认采用异步执行
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
        startExpireTask(seq,channel);
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
        //同步请求无需启动延时监听
        return workerGroup.submit(() -> responseMap.lockAndGetResponse(seq, timeout));
    }


    /**
     * 启动一个过期检测任务，作用于请求发送后的超时检测
     * @param seq 检测的请求序号
     * @param channel 请求的信道
     * @return future类型用于表示该请求对应的响应状态
     */
    private Future<?> startExpireTask(int seq,Channel channel)
    {
        ExpireTask task = new ExpireTask(channel, seq);
        return workerGroup.schedule(task, timeout, TimeUnit.MILLISECONDS);
    }










}
