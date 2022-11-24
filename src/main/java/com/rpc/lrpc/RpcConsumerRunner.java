package com.rpc.lrpc;

import com.rpc.lrpc.net.ChannelPool;
import com.rpc.lrpc.net.RPCRequestSender;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@ConditionalOnBean(RPCRequestSender.class)
@Configuration
@ComponentScan("com.rpc.lrpc")
@Slf4j
public class RpcConsumerRunner implements ApplicationRunner {
    @Autowired
    RPCRequestSender sender;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Consumer start....");
    }
}
