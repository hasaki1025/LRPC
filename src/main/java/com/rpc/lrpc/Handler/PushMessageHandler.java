package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.message.BroadcastMessage;
import com.rpc.lrpc.message.Content.Broadcast.PushContent;
import com.rpc.lrpc.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@ConditionalOnBean(RpcConsumer.class)
@ChannelHandler.Sharable
@Order(3)
public class PushMessageHandler extends SimpleChannelInboundHandler<BroadcastMessage<PushContent>> {

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Push);
    }


    @Autowired
    RpcConsumer consumer;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BroadcastMessage<PushContent> msg) throws Exception {
        PushContent content = msg.content();
        consumer.addService(content.getRpcService(),content.getRpcAddress());
        log.info("new Consumer Service List {}", Arrays.stream(consumer.getAllAddress()).toArray());
    }
}
