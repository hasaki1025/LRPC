package com.rpc.lrpc.Context;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class RPCContextConfig implements ApplicationRunner {


    @Autowired
    private Environment environment;

    @Bean
    @ConditionalOnProperty(name={"RPC.Provider.ServiceName","RPC.Provider.port","RPC.Server.Host","RPC.Server.port"})
    RPCServiceProvider rPCServiceRProvider()
    {
        return new RPCServiceRProviderContext(
                environment.getProperty("RPC.Provider.ServiceName",String.class),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty("RPC.Provider.port"))),
                environment.getProperty("RPC.Server.Host", String.class),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty("RPC.Server.port")))
        );
    }


    @Bean
    @ConditionalOnProperty(name = {
            "RPC.Server.port","RPC.Server.Host"
    })
    RpcConsumer rpcConsumer()
    {
        return new RpcConsumerContext(environment.getProperty("RPC.Server.Host",String.class),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty("RPC.Server.port"))));
    }

    @Bean
    @ConditionalOnProperty(name = {
            "RPC.Register.port"
    })
    RpcRegister register()
    {
        return new RpcRegisterContext(Integer.parseInt(Objects.requireNonNull(environment.getProperty("RPC.Register.port"))));
    }

    @Autowired(required = false)
    RPCServiceProvider rpcServiceProvider;
    @Autowired(required = false)
    RpcRegister rpcRegister;
    @Autowired(required = false)
    RpcConsumer rpcConsumer;
    @Autowired
    ConfigurableApplicationContext applicationContext;
    @Override
    //TODO 注册中心开始监听注册请求
    //TODO 消费者拉取服务列表
    //TODO 服务提供者从容器中获取并注册服务
    public void run(ApplicationArguments args) throws Exception {

    }
}
