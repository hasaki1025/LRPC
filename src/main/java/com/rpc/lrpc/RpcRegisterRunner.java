package com.rpc.lrpc;

import com.rpc.lrpc.net.RegisterServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@ConditionalOnBean(RegisterServer.class)
@Configuration
@ComponentScan("com.rpc.lrpc")
@Slf4j
public class RpcRegisterRunner implements ApplicationRunner {

    @Autowired
    RegisterServer server;
    @Override
    public void run(ApplicationArguments args) throws Exception {
       server.init();
       log.info("Register Start.....");
    }
}
