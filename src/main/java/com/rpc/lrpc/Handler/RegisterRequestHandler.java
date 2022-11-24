package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultPushServicesRequest;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.net.DokiDokiMap;
import com.rpc.lrpc.net.RegisterServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    DokiDokiMap dokiDokiMap;
    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup workerGroup;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<RegisterRequest> msg) throws JsonProcessingException {
        SimpleResponse response = new SimpleResponse();
        try {
            rpcRegister.registerService(msg.content().getRpcService(),msg.content().getRpcURL());
            //TODO 发送广播通知其他Channel
            workerGroup.submit(()->{
                DefaultPushServicesRequest request = new DefaultPushServicesRequest();
                request.setRpcService(msg.content().getRpcService());
                request.setRpcURL(msg.content().getRpcURL());
                RegisterServer.broadcastMessage(new RequestMessage<PushServicesRequest>(CommandType.Push,MessageType.request,request));
            });
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Register,MessageType.response,response,msg.getSeq()));


    }
}
