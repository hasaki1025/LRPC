package com.rpc.lrpc.Handler;

import com.rpc.lrpc.message.Content.Request.CallServicesRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RequestMessage<CallServicesRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<CallServicesRequest> msg) throws Exception {
        CallServicesRequest request = msg.content();
        //TODO 在此处获取Mapping对应的方法和参数等信息
    }
}
