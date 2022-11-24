package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.Exception.IncorrectMagicNumberException;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.net.ResponseMap;
import com.rpc.lrpc.net.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
@Order(0)
public class MessageCodec extends MessageToMessageCodec<ByteBuf,DefaultMessage> {

    @Autowired
    ResponseMap responseMap;


    @Override
    protected void encode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        MessageUtil.messageToByteBuf(msg,buffer);
        if (msg.getMessageType().equals(MessageType.request)
                && !msg.getCommandType().equals(CommandType.DokiDoki)
                && !msg.getCommandType().equals(CommandType.Call))
        {
            //需要等待的响应
            //心跳发送不需要响应
            responseMap.addWaitingRequest(msg.getSeq());
        }
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {

            Message message = MessageUtil.byteToMessage(msg);
            if (message.getMessageType().equals(MessageType.response))
            {
                //序号检查
                if (!responseMap.stillWaiting(message.getSeq())) {
                    throw new RuntimeException("not match Request of this Response");
                }
                if (!message.getCommandType().equals(CommandType.Call)) {
                    responseMap.removeWaitingRequest(message.getSeq());
                }
            }
            out.add(message);
        } catch (IncorrectMagicNumberException e) {
            throw new RuntimeException(e);
        }
    }
}
