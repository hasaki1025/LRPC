package com.rpc.lrpc;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.net.RegisterServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@ConditionalOnBean(RpcRegister.class)
@Component
@ComponentScan("com.rpc.lrpc")
@Slf4j
@Order(1)
public class RpcRegisterRunner implements ApplicationRunner {

    @Autowired
    RegisterServer server;
    @Override
    public void run(ApplicationArguments args) throws Exception {
       server.init();
       log.info("Register Start.....");
    }
}
