package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.RpcRole;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcChannelInitializer extends ChannelInitializer<Channel> {


    @Autowired
    List<ChannelHandler> handlersChain;


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
        //设置计数器
        ch.attr(AttributeKey.valueOf("SeqCounter")).set(new AtomicInteger());
    }
}
