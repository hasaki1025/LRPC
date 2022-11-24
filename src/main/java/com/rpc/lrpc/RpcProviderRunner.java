package com.rpc.lrpc;

import com.rpc.lrpc.net.ProviderClient;
import com.rpc.lrpc.net.ServiceProviderServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@ConditionalOnBean(ProviderClient.class)
@Configuration
@ComponentScan("com.rpc.lrpc")
@Slf4j
@Order(2)
public class RpcProviderRunner implements ApplicationRunner {
    @Autowired
    ProviderClient client;
    @Autowired
    ServiceProviderServer server;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.init();
        server.init();
        log.info("ServiceProvider start....");
    }
}
