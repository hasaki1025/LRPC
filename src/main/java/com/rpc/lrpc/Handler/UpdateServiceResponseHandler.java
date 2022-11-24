package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.message.Content.Response.UpdateServiceResponse;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ConditionalOnBean(RpcConsumer.class)
@Order(3)
@Slf4j
@ChannelHandler.Sharable
public class UpdateServiceResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<UpdateServiceResponse>> {

    @Autowired
    RpcConsumer rpcConsumer;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<UpdateServiceResponse> msg) throws Exception {
        if (!msg.content().hasException()) {
            RpcURL[] urls = msg.content().getRpcUrls();
            RpcService rpcService = msg.content().getRpcService();
            HashMap<RpcService, RpcURL[]> map = new HashMap<>();
            map.put(rpcService,urls);
            rpcConsumer.addServices(map);
        }
        else {
            msg.content().getException().printStackTrace();
        }
    }
}
