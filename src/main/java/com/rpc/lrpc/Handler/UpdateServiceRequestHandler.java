package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.message.Content.Request.UpdateServiceRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@ConditionalOnBean(RpcConsumer.class)
@Slf4j
@ChannelHandler.Sharable
public class UpdateServiceRequestHandler extends SimpleChannelInboundHandler<RequestMessage<UpdateServiceRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<UpdateServiceRequest> msg) throws Exception {

    }
}
