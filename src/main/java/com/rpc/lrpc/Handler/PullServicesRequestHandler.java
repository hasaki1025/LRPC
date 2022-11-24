package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.Content.Response.DefaultPullServicesResponse;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
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
@ConditionalOnBean(RpcRegister.class)
@Component
@ChannelHandler.Sharable
@Order(3)
@Slf4j
public class PullServicesRequestHandler extends SimpleChannelInboundHandler<RequestMessage<PullServicesRequest>> {
    @Autowired
    RpcRegister rpcRegister;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<PullServicesRequest> msg) throws Exception {
        PullServicesResponse response = new DefaultPullServicesResponse();
        try{
            response.addRpcService(rpcRegister.getRpcServiceMap());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Pull, msg.getSeq(), MessageType.response, response));
    }
}
