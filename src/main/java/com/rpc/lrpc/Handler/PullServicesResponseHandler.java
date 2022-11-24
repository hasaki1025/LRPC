package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.net.ChannelResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
@Order(3)
@ChannelHandler.Sharable
@ConditionalOnBean(RpcConsumer.class)
@Slf4j

public class PullServicesResponseHandler extends SimpleChannelInboundHandler<ResponseMessage<PullServicesResponse>> {

    @Autowired
    RpcConsumer rpcConsumer;

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Pull);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage<PullServicesResponse> msg) throws Exception {
        PullServicesResponse content = msg.content();
        rpcConsumer.addServices(content.getAddressMap(),content.getMappingMap());
        Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP));
        ChannelResponse responseMap = (ChannelResponse) attr.get();
        log.info("get Pull Response");
        responseMap.putResponse(msg.getSeq(), content);
    }



}
