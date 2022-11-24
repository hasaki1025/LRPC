package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.DefaultMessage;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ChannelHandler.Sharable
@Order(1)
@Slf4j
public class RequestSerializableHandler extends MessageToMessageCodec<DefaultMessage, RequestMessage<RequestContent>> {



    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage<RequestContent> msg, List<Object> out) throws Exception {
        //设置SEQ
        if (msg.getSeq()==0)
        {
            AtomicInteger seqCounter = (AtomicInteger) ctx.channel().attr(AttributeKey.valueOf(MessageUtil.SEQ_COUNTER_NAME)).get();
            msg.setSeq(seqCounter.getAndIncrement());
        }
        log.info("send {} Request",msg.getCommandType().name());
        out.add(MessageUtil.requestToDefaultMessage(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        if(MessageType.request.equals(msg.getMessageType()))
        {
            MessageType messageType = msg.getMessageType();
            if (!MessageType.request.equals(messageType))
            {
                out.add(msg);
                return;
            }
            log.info("get {} Request",msg.getCommandType().name());
            //注意从这之后就只有
            out.add(MessageUtil.defaultMessageToRequest(msg));
        }
        else {
            out.add(msg);
        }

    }

}
