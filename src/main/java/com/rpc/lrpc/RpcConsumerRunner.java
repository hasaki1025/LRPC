package com.rpc.lrpc;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.net.ChannelPool;
import com.rpc.lrpc.net.ConsumerClient;
import com.rpc.lrpc.net.RPCRequestSender;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@ConditionalOnBean(RPCRequestSender.class)
@Configuration
@ComponentScan("com.rpc.lrpc")
@Slf4j
public class RpcConsumerRunner implements ApplicationRunner {
    @Autowired
    RPCRequestSender sender;
    @Autowired
    ChannelPool channelPool;
    @Autowired
    RpcConsumer consumer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sender.init();
    }
}
