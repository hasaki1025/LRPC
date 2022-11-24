package com.rpc.lrpc;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Request.DefaultPullServicesRequest;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.Content.Response.DefaultPullServicesResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcUrl;
import com.rpc.lrpc.net.RPCRequestSender;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

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
    @Autowired
    RPCRequestSender sender;
    @Autowired
    RpcConsumer consumer;


    @Autowired
    List<ChannelHandler> handlerList;

    @Test
    void testChannelHandler() {
        ArrayList<ChannelHandler> list = new ArrayList<>();
        list.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        list.add(new LoggingHandler(LogLevel.INFO));
        list.addAll(handlerList);
        for (ChannelHandler handler : handlerList) {
            System.out.println(handler);
        }

        EmbeddedChannel channel = new EmbeddedChannel(list.toArray(new ChannelHandler[0]));
        channel.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).set(new AtomicInteger(1));
        PullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeOutbound(new RequestMessage<>(CommandType.Pull, MessageType.request, request));

    }
    @Test
    void testChannelHandlerIn() {
        ArrayList<ChannelHandler> list = new ArrayList<>();
        list.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        list.add(new LoggingHandler(LogLevel.INFO));
        list.addAll(handlerList);
        for (ChannelHandler handler : handlerList) {
            System.out.println(handler);
        }


        EmbeddedChannel channel = new EmbeddedChannel(list.toArray(new ChannelHandler[0]));
        channel.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).set(new AtomicInteger(1));
        PullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeInbound(new RequestMessage<>(CommandType.Pull, MessageType.request, request));

    }

    @Test
    void testConsumerSericeList()
    {
        for (RpcAddress address : consumer.getAllAddress()) {
            System.out.println(address);
        }
    }

    @Test
    void testSendCallMessage() {
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sender.callSync("rpc://nihao:test", "123"));
    }
}
