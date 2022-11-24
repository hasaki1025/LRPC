package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultPushServiceResponse;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@ConditionalOnBean(RpcConsumer.class)
@Component
@ChannelHandler.Sharable
@Order(3)
@Slf4j
public class PushServicesRequestHandler extends SimpleChannelInboundHandler<RequestMessage<PushServicesRequest>> {

    @Autowired
    RpcConsumer rpcConsumer;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<PushServicesRequest> msg) throws Exception {
        rpcConsumer.addServices(msg.content().getServicesMap());
        DefaultPushServiceResponse response = new DefaultPushServiceResponse();
        String s = new ObjectMapper().writeValueAsString(response);
        ctx.writeAndFlush(new DefaultMessage(CommandType.Push, s.getBytes(StandardCharsets.UTF_8).length, msg.getSeq(), MessageType.response, s));
    }
}
