package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
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

@Slf4j
@Component
@ConditionalOnBean(RpcRegister.class)
@ChannelHandler.Sharable
@Order(3)
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RequestMessage<RegisterRequest>> {

    @Autowired
    RpcRegister rpcRegister;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<RegisterRequest> msg) throws JsonProcessingException {
        SimpleResponse response = new SimpleResponse();
        try {
            rpcRegister.registerService(msg.content().getRpcService(),msg.content().getRpcURL());
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Simple, msg.getSeq(), MessageType.response, response));
    }
}
