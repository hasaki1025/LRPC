package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
import com.rpc.lrpc.message.Content.Response.PushServiceResponse;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.net.ResponseMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
//TODO 是否需要添加条件注解
@Component
@ChannelHandler.Sharable
@Slf4j
@Order(4)
public class SimpleResonseHandler extends SimpleChannelInboundHandler<ResponseMessage<SimpleResponse>> {
    @Autowired
    ResponseMap responseMap;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<SimpleResponse> msg) throws Exception {
        log.info("[{}] Request get Response",msg.getSeq());
        if (msg.content().hasException()) {
            msg.content().getException().printStackTrace();
        }
        responseMap.removeWaitingRequest(msg.getSeq());
    }
}
