package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Exception.NoCommandTypeMatchException;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.Content.Response.*;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ChannelHandler.Sharable
@Order(2)
public class ResponseDispatchHandler extends MessageToMessageDecoder<ResponseMessage<ResponseContent>> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ResponseMessage<ResponseContent> msg, List<Object> out) throws Exception {
        CommandType type = msg.getCommandType();
        assert type!=null;
        if (type.equals(CommandType.Call)) {
            out.add(new ResponseMessage<>(msg, (CallServicesResponse) msg.content()));
        } else if (type.equals(CommandType.Register)) {
            out.add(new ResponseMessage<>(msg, (RegisterResponse) msg.content()));
        }else if (type.equals(CommandType.Pull)) {
            out.add(new ResponseMessage<>(msg, (PullServicesResponse) msg.content()));
        } else if (type.equals(CommandType.Update)) {
            out.add(new ResponseMessage<>(msg, (UpdateServiceResponse) msg.content()));
        } else if (type.equals(CommandType.DokiDoki)) {
            out.add(new RequestMessage<>(msg, (DokiDokiRequest) msg.content()));
        } else {
            throw new NoCommandTypeMatchException();
        }
    }
}
