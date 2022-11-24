package com.rpc.lrpc.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ConsumerChannelInitializer extends ChannelInitializer<Channel> {


    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup workerGroup;


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        ch.pipeline().addLast(new StringEncoder());
        log.info("connect successfully....");
        workerGroup.scheduleWithFixedDelay(()->{
            System.out.println("gg");
        },0,2, TimeUnit.SECONDS);
    }
}
