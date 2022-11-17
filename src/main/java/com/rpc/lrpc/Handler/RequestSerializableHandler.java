package com.rpc.lrpc.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class RequestSerializableHandler extends MessageToMessageCodec<DefaultMessage, RequestMessage<RequestContent>> {




    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage<RequestContent> msg, List<Object> out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        String content = msg.content();
        SerializableType type = msg.getSerializableType();
        CommandType commandType = msg.getCommandType();
        MessageType messageType = msg.getMessageType();
        if (!MessageType.request.equals(messageType))
        {
            out.add(msg);
            return;
        }
        Object value = new ObjectMapper().readValue(msg.content(), CommandType.requestTypeClass[msg.getCommandType().getValue()]);
        //注意从这之后就只有
        out.add(new RequestMessage<>(msg, (RequestContent) value));
    }

}
