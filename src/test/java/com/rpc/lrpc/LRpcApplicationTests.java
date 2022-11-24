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
    RpcConsumer consumer;


    @Autowired
    List<ChannelHandler> handlerList;

    @Test
    void testChannelHandler() {
        EmbeddedChannel channel = getEmbeddedChannel();
        PullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeOutbound(new RequestMessage<>(CommandType.Pull, MessageType.request, request));

    }
    @Test
    void testChannelHandlerIn() {
        EmbeddedChannel channel = getEmbeddedChannel();
        PullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeInbound(new RequestMessage<>(CommandType.Pull, MessageType.request, request));
    }

    private EmbeddedChannel getEmbeddedChannel() {
        ArrayList<ChannelHandler> list = new ArrayList<>();
        list.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        list.add(new LoggingHandler(LogLevel.INFO));
        list.addAll(handlerList);


        EmbeddedChannel channel = new EmbeddedChannel(list.toArray(new ChannelHandler[0]));
        channel.attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).set(new AtomicInteger(1));
        channel.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).set(new ChannelResponse());
        return channel;
    }


    @Autowired
    RpcRegister register;
    @Autowired
    RPCServiceProvider rpcServiceProvider;
    @Test
    void testRegisterServices() {
        /*RpcAddress rpcUrl = rpcServiceProvider.getRpcUrl();
        RpcService rpcService = rpcServiceProvider.getRpcService();
        DefaultRegisterRequest request = new DefaultRegisterRequest();
        request.setRpcService(rpcService);
        request.setRpcAddress(rpcUrl);
        getEmbeddedChannel()
                .writeInbound(new RequestMessage<RegisterRequest>(CommandType.Register,MessageType.request,request));*/
        for (RpcAddress address : register.getAllUrl()) {
            System.out.println(address);
        }
    }

    @Test
    void testProviderMethod() {
        System.out.println(rpcServiceProvider.getServiceName());

        for (RpcMapping mapping : rpcServiceProvider.getMappings()) {
            System.out.println(mapping);
        }
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
        //System.out.println(sender.callSync("rpc://nihao:test", "123"));
    }

    @Test
    void testGetPullResponse() {
        DefaultPullServicesResponse response = new DefaultPullServicesResponse();


        Map<RpcService, RpcAddress[]> map=new HashMap<>();
        map.put( rpcServiceProvider.getRpcService(),new RpcAddress[]{rpcServiceProvider.getRpcUrl()});
        response.addRpcService(map);

        EmbeddedChannel channel = getEmbeddedChannel();
        PullServicesRequest request = new DefaultPullServicesRequest();
        channel.writeOutbound(new RequestMessage<>(CommandType.Pull, MessageType.request, request));

        getEmbeddedChannel()
                .writeInbound(new ResponseMessage<PullServicesResponse>(CommandType.Pull,MessageType.response,response,1));
        for (RpcAddress address : consumer.getAllAddress()) {

            System.out.println(address);
        }
    }

    @Test
    void name() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(new DefaultPullServicesResponse()));
    }
}
