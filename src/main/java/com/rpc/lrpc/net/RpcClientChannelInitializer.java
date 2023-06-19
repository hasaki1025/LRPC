package com.rpc.lrpc.net;

import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Message;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client信道初始化类
 */
@Slf4j
public class RpcClientChannelInitializer extends ChannelInitializer<Channel> {


    /**
     * 注入的处理器链
     */
    List<ChannelHandler> handlersChain;



    public RpcClientChannelInitializer(List<ChannelHandler> handlersChain) {
        this.handlersChain = handlersChain;
    }

    /**
     * 初始化信道
     * @param ch 信道
     * @throws Exception 父类异常抛出
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        ch.pipeline().addLast(handlersChain.toArray(new ChannelHandler[0]));
        //设置计数器
        log.info("connect from [{}] to [{}]",ch.localAddress(),ch.remoteAddress());
        ch.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).set(new AtomicInteger(1));
        ch.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).set(new ChannelResponse());
    }
}
