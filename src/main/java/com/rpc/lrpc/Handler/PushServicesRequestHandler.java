package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.Message;
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

@ConditionalOnBean(RpcConsumer.class)
@Component
@ChannelHandler.Sharable
@Order(3)
@Slf4j
public class PushServicesRequestHandler extends SimpleChannelInboundHandler<RequestMessage<PushServicesRequest>> {

    @Autowired
    RpcConsumer rpcConsumer;
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Push);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<PushServicesRequest> msg) throws Exception {
        SimpleResponse response = new SimpleResponse();
        try{
            rpcConsumer.addService(msg.content().getRpcService(),msg.content().getRpcAddress());
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Push,MessageType.response,response,msg.getSeq()));
    }
}
