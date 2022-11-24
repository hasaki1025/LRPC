package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.message.BroadcastMessage;
import com.rpc.lrpc.message.Content.Broadcast.DeleteContent;
import com.rpc.lrpc.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnBean(RpcConsumer.class)
@ChannelHandler.Sharable
@Order(3)
public class DeleteMessageHandler extends SimpleChannelInboundHandler<BroadcastMessage<DeleteContent>> {

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Delete);
    }
    @Autowired
    RpcConsumer rpcConsumer;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BroadcastMessage<DeleteContent> msg) throws Exception {
        rpcConsumer.removeAddress(msg.content().getAddress());
    }
}
