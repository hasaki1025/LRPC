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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@ChannelHandler.Sharable
@Order(1)
public class ResponseSerializableHandler extends MessageToMessageCodec<DefaultMessage, ResponseMessage<ResponseContent>> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage<ResponseContent> msg, List<Object> out) throws Exception {
        byte[] bytes = new ObjectMapper().writeValueAsString(msg.content()).getBytes(StandardCharsets.UTF_8);
        //一定要设置
        msg.setSize(bytes.length);
        out.add(MessageUtil.rpcResponseToDefaultMessage(msg, new String(bytes, StandardCharsets.UTF_8)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        if(MessageType.response.equals(msg.getMessageType()))
        {
            String content = msg.content();
            SerializableType type = msg.getSerializableType();
            CommandType commandType = msg.getCommandType();
            Object value = new ObjectMapper().readValue(msg.content(), CommandType.responseTypeClass[msg.getCommandType().getValue()]);
            //注意从这之后就只有
            out.add(new ResponseMessage<>(msg, (ResponseContent) value));
        }
        else {
            out.add(msg);
        }
    }
}
