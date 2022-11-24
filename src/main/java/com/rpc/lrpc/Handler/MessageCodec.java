package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Exception.IncorrectMagicNumberException;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.net.ResponseMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
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
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {

            Message message = MessageUtil.byteToMessage(msg);
            //序号检查
            if (!responseMap.stillWaiting(message.getSeq()))
            {
                responseMap.removeWaitingRequest(message.getSeq());
                throw new RuntimeException("not match Request of this Response");
            }
            out.add(message);
        } catch (IncorrectMagicNumberException e) {
            throw new RuntimeException(e);
        }
    }
}
