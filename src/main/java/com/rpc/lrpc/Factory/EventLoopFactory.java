package com.rpc.lrpc.Factory;

import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Util.ConditionServer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventLoopFactory {

    @Value("${RPC.Config.ChannelType}")
    String type;
    @Bean
    EventLoopGroup group()
    {
        if (type.equals(ChannelType.NIO.name()))
        {
            return new NioEventLoopGroup();
        } else if (type.equals(ChannelType.EPOLL.name())) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Bean
    @Conditional(ConditionServer.class)
    EventLoopGroup chirdGroup()
    {
        if (type.equals(ChannelType.NIO.name()))
        {
            return new NioEventLoopGroup();
        } else if (type.equals(ChannelType.EPOLL.name())) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Bean
    DefaultEventLoopGroup workerGroup()
    {
        return new DefaultEventLoopGroup();
    }


}
