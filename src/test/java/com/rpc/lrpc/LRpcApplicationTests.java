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


    @Autowired
    ConfigurableApplicationContext context;

   @Autowired
   List<ChannelHandler> channelHandlers;

   @Autowired
    TestController testController;
    @Test
    void testAutowire() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method test = TestController.class.getDeclaredMethod("test", String.class);
        test.invoke(testController,"123");
    }

    @Test
        //魔数（4）-版本号（1）-序列化算法（1）-消息类型（1）-指令类型(1)-请求序号(4)-正文长度(4)-消息本体
    //测试接受请求
    void getCallRequest() throws UnknownHostException, JsonProcessingException {
       /* ArrayList<ChannelHandler> list = new ArrayList<>();
        list.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0));
        list.add(new LoggingHandler(LogLevel.INFO));
        list.addAll(channelHandlers);

        EmbeddedChannel channel = new EmbeddedChannel(list.toArray(new ChannelHandler[0]));
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setMapping("/test");
        request.setParamValues(new String[]{"123"});
        String s = new ObjectMapper().writeValueAsString(request);
        //DefaultMessage message = new DefaultMessage(CommandType.);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        MessageUtil.messageToByteBuf(message,buffer);
        channel.writeInbound(buffer);*/
    }


    @Autowired
    ResponseMap responseMap;
    @Test
    //测试接受响应
    void getCallResponse() throws JsonProcessingException {
        ArrayList<ChannelHandler> list = new ArrayList<>();
        list.add(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0));
        list.add(new LoggingHandler(LogLevel.INFO));
        list.addAll(channelHandlers);

        EmbeddedChannel channel = new EmbeddedChannel(list.toArray(new ChannelHandler[0]));
        DefaultCallServicesResponse response = new DefaultCallServicesResponse();
        response.setResult("123");
        String s = new ObjectMapper().writeValueAsString(response);
        DefaultMessage message = new DefaultMessage(CommandType.Call, s.getBytes(StandardCharsets.UTF_8).length, 1, MessageType.response, s,RpcRole.Provider);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        MessageUtil.messageToByteBuf(message,buffer);
        channel.writeInbound(buffer);
        System.out.println(responseMap.getResponse(1));
    }

    @Test
    void testLoaclhost() {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }



}
