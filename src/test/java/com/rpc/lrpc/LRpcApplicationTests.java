package com.rpc.lrpc;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Context.RpcServiceBeanPostProcessor;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class LRpcApplicationTests {


    @Autowired
    ConfigurableApplicationContext context;
    @Test
    void contextLoads() {
        System.out.println(context.getBean(RpcConsumer.class));
        System.out.println(context.getBean(RpcRegister.class));
        System.out.println(context.getBean(RPCServiceProvider.class));
        System.out.println(context.getBean(RpcServiceBeanPostProcessor.class));
    }

}
