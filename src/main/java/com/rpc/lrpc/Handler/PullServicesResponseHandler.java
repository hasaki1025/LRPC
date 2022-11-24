package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
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
public class PullServicesResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<PullServicesResponse>> {

    @Autowired
    RpcConsumer rpcConsumer;
    @Autowired
    ResponseMap responseMap;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<PullServicesResponse> msg) throws Exception {
        rpcConsumer.addServices(msg.content().getRpcServiceMap());
    }
}
