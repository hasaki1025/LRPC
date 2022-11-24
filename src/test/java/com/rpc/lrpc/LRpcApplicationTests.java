package com.rpc.lrpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.*;
import com.rpc.lrpc.message.Content.Request.DefaultPullServicesRequest;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.Content.Response.DefaultPullServicesResponse;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
import com.rpc.lrpc.net.ChannelResponse;
import com.rpc.lrpc.net.RpcCallRequestSender;
import com.rpc.lrpc.net.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@SpringBootTest
public class LRpcApplicationTests {




    @Autowired
    RpcCallRequestSender sender;

    @Test
    void testCallSender() {
        Optional<String> s = sender.callSync("rpc://nihao:test", String.class, "123");
        if (s.isPresent())
        {
            System.out.println(s);
        }
    }
}
