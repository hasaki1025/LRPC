package com.rpc.lrpc.Factory;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class LogginHandlerFactory {

    @Bean
    @Order(0)
    LoggingHandler loggingHandler( @Value("${RPC.Config.loggingLevel}") String loggingLevel)
    {
        return new LoggingHandler(LogLevel.valueOf(loggingLevel));
    }
}
