package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Exception.NoCommandTypeMatchException;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RequestDispatchHandler extends MessageToMessageDecoder<RequestMessage<RequestContent>> {

    @Override
    protected void decode(ChannelHandlerContext ctx, RequestMessage<RequestContent> msg, List<Object> out) throws Exception {

        CommandType type = msg.getCommandType();
        assert type!=null;
        if (type.equals(CommandType.Call)) {
            out.add(new RequestMessage<CallServicesRequest>(msg, msg.content()));
        } else if (type.equals(CommandType.Register)) {
            out.add(new RequestMessage<RegisterRequest>(msg, msg.content()));
        }else if (type.equals(CommandType.Pull)) {
            out.add(new RequestMessage<PullServicesRequest>(msg, msg.content()));
        } else if (type.equals(CommandType.Update)) {
            out.add(new RequestMessage<UpdateServiceRequest>(msg, msg.content()));
        } else if (type.equals(CommandType.DokiDoki)) {
            out.add(new RequestMessage<DokiDokiRequest>(msg, msg.content()));
        } else {
            throw new NoCommandTypeMatchException();
        }
    }
}
