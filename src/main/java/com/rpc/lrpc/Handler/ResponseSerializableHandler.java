package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Content.Response.ResponseContent;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ChannelHandler.Sharable
@Order(1)
@Slf4j

public class ResponseSerializableHandler extends MessageToMessageCodec<DefaultMessage, ResponseMessage<ResponseContent>> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage<ResponseContent> msg, List<Object> out) throws Exception {
        log.info("send {} Response",msg.getCommandType().name());
        out.add(MessageUtil.rpcResponseToDefaultMessage(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        if(MessageType.response.equals(msg.getMessageType()))
        {
            //注意从这之后就只有
            log.info("get {} Response",msg.getCommandType());
            out.add(MessageUtil.DefaultMessageToResponse(msg));
        }
        else {
            out.add(msg);
        }
    }
}
