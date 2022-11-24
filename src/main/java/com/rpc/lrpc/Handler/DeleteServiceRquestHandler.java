package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DeleteServiceRequest;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@ConditionalOnBean(RpcConsumer.class)
@Component
@Slf4j
@ChannelHandler.Sharable
public class DeleteServiceRquestHandler extends SimpleChannelInboundHandler<RequestMessage<DeleteServiceRequest>> {

    @Autowired
    RpcConsumer rpcConsumer;
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Delete);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<DeleteServiceRequest> msg) throws Exception {
        SimpleResponse response = new SimpleResponse();
        try
        {
            RpcAddress address = msg.content().getAddress();
            rpcConsumer.removeAddress(address);
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Simple, MessageType.response, response, msg.getSeq()));
    }
}
