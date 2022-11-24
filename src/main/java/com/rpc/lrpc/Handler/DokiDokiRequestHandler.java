package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.net.DokiDokiMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RpcRegister.class)
@Slf4j
@ChannelHandler.Sharable
@Order(3)

public class DokiDokiRequestHandler extends SimpleChannelInboundHandler<RequestMessage<DokiDokiRequest>> {


    @Autowired
    DokiDokiMap dokiDokiMap;
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.DokiDoki);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<DokiDokiRequest> msg) throws Exception {
        try
        {
            dokiDokiMap.updateOrAddLastDokiTime(msg.content().getRpcAddress());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
