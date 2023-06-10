package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.message.Content.Response.SimpleResponse;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
//TODO 是否需要添加条件注解
@Component
@ChannelHandler.Sharable
@Slf4j
@Order(4)
public class SimpleResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<SimpleResponse>> {


    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message)msg).getCommandType().equals(CommandType.Simple);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<SimpleResponse> msg) throws Exception {
        log.info("[{}] Request get Response",msg.getSeq());
        if (msg.content().hasException()) {
            msg.content().getException().printStackTrace();
        }
    }
}
