package com.rpc.lrpc;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.net.ConsumerChannelPool;
import com.rpc.lrpc.net.ConsumerToRegisterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



@Component
@ConditionalOnBean(RpcConsumer.class)
@ComponentScan("com.rpc.lrpc")
@Slf4j
public class RpcConsumerRunner implements ApplicationRunner {

    @Autowired
    ConsumerChannelPool consumerChannelPool;
    @Autowired
    ConsumerToRegisterClient client;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.init();
        log.info("Consumer init....");
    }
}
