package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultPushServiceResponse;
import com.rpc.lrpc.message.Content.Response.PushServiceResponse;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
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
        SimpleResponse response = new SimpleResponse();
        try{
            rpcConsumer.addServices(msg.content().getServicesMap());
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Simple, msg.getSeq(), MessageType.response, response, RpcRole.Consumer));
    }
}
