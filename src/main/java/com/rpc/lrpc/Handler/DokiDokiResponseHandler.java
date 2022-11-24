package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.message.Content.Response.DokiDokiResponse;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.net.DokiDokiMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RpcRegister.class)
@ChannelHandler.Sharable
@Slf4j
@Order(3)
public class DokiDokiResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<DokiDokiResponse>> {

    @Autowired
    DokiDokiMap dokiDokiMap;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<DokiDokiResponse> msg) throws Exception {
        dokiDokiMap.updateOrAddLastDokiTime(msg.content().getRpcURL());
    }
}
