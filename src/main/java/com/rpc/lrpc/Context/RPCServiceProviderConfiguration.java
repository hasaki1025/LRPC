package com.rpc.lrpc.Context;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@ConditionalOnBean(RPCServiceProvider.class)
@Configuration
public class RPCServiceProviderConfiguration implements ApplicationRunner {

    @Autowired
    RPCServiceProvider provider;
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
