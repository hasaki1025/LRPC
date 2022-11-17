package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Exception.IncorrectMagicNumberException;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
public class MessageCodec extends MessageToMessageCodec<ByteBuf,DefaultMessage> {



    @Override
    protected void encode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            out.add(MessageUtil.byteToMessage(msg));
        } catch (IncorrectMagicNumberException e) {
            throw new RuntimeException(e);
        }
    }
}
