package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Exception.IncorrectMagicNumberException;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.net.ChannelResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
@Order(1)
@Slf4j
public class MessageCodec extends MessageToMessageCodec<ByteBuf,DefaultMessage> {




    @Override
    protected void encode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        MessageUtil.messageToByteBuf(msg,buffer);
        if (msg.getMessageType().equals(MessageType.request)
                && !msg.getCommandType().equals(CommandType.DokiDoki))
        {
            //只有Request需要响应
            //心跳发送不需要响应
            Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP));
            ChannelResponse channelResponse = (ChannelResponse) attr.get();
            channelResponse.addWaitRequest(msg.getSeq());
        }
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {

            Message message = MessageUtil.byteToMessage(msg);
            if (message.getMessageType().equals(MessageType.response))
            {
                ChannelResponse responseMap = (ChannelResponse) ctx.channel().attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
                //序号检查
                int seq = message.getSeq();
                if (!responseMap.stillWaiting(seq)) {
                    throw new RuntimeException("not match Request of this Response");
                }
            }
            out.add(message);
        } catch (IncorrectMagicNumberException e) {
            throw new RuntimeException(e);
        }
    }
}
