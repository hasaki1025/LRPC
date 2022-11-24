package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.net.ChannelResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@ChannelHandler.Sharable

@ConditionalOnBean(RpcConsumer.class)
@Slf4j
public class CallServiceResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<CallServicesResponse>> {



    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Call);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<CallServicesResponse> msg) throws Exception {
        try {
            log.info("[{}] Request get Response",msg.getSeq());
            if (msg.content().hasException()) {
                msg.content().getException().printStackTrace();
            }else {
                ChannelResponse response = (ChannelResponse) ctx.channel().attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
                response.putResponse(msg.getSeq(),msg.content());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
