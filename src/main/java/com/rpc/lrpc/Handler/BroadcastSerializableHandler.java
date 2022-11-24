package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ChannelHandler.Sharable
@Order(2)
@Slf4j
@ConditionalOnBean(RpcConsumer.class)
public class BroadcastSerializableHandler extends MessageToMessageDecoder<DefaultMessage> {


    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message)msg).getMessageType().equals(MessageType.broadcast);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        if (msg.getMessageType().equals(MessageType.broadcast)){
            log.info("get {} BroadcastMessage from [{}]",msg.getCommandType(),ctx.channel().remoteAddress());
            out.add(MessageUtil.defaultMessageToBroadcastMessage(msg));
        }
        else {
            out.add(msg);
        }
    }
}
