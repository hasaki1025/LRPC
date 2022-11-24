package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
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

@Component
@Order(3)
@ChannelHandler.Sharable
@ConditionalOnBean(RpcConsumer.class)
@Slf4j
public class CallServiceResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<CallServicesResponse>> {


    @Autowired
    ResponseMap responseMap;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<CallServicesResponse> msg) throws Exception {
        log.info("[{}] Request get Response",msg.getSeq());
        if (msg.content().hasException()) {
            msg.content().getException().printStackTrace();
            responseMap.removeWaitingRequest(msg.getSeq());
        }else {
            responseMap.putResponse(msg.getSeq(),msg.content());
        }
    }
}
