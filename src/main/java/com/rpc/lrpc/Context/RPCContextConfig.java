package com.rpc.lrpc.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class RPCContextConfig {


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

}
