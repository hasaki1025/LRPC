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


public class RpcClientChannelInitializer extends ChannelInitializer<Channel> {



    List<ChannelHandler> handlersChain;



    public RpcClientChannelInitializer(List<ChannelHandler> handlersChain) {
        this.handlersChain = handlersChain;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
        //设置计数器
        ch.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).set(new AtomicInteger(1));
        ch.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).set(new ChannelResponse());
    }
}
