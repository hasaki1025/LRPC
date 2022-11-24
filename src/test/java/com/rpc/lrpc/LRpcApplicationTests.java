package com.rpc.lrpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Request.DefaultCallServicesRequest;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcUrl;
import com.rpc.lrpc.net.ResponseMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class LRpcApplicationTests {


    @Test
    void testPaseUrl() {
        RpcUrl url = MessageUtil.parseUrl("rpc://127.0.0.1:8080/serviceName");
        System.out.println(url.getAddress().getHost());
        System.out.println(url.getAddress().getPort());
        System.out.println(url.getAddress().getServiceName());
        System.out.println(url.getMapping());
    }

    @Test
    void testPaseAdress() {
        RpcAddress address = MessageUtil.parseAddress("rpc://127.0.0.1:8080");
        System.out.println(address.getHost());
        System.out.println(address.getPort());
        System.out.println(address.getServiceName());
    }
}
