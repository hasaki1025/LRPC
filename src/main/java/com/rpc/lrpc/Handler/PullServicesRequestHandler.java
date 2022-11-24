package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.Content.Response.DefaultPullServicesResponse;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.net.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@ConditionalOnBean(RpcRegister.class)
@Component
@ChannelHandler.Sharable
@Order(3)
@Slf4j
public class PullServicesRequestHandler extends SimpleChannelInboundHandler<RequestMessage<PullServicesRequest>> {
    @Autowired
    RpcRegister rpcRegister;
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getCommandType().equals(CommandType.Pull);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<PullServicesRequest> msg) throws Exception {
        PullServicesResponse response = new DefaultPullServicesResponse();
        try
        {
            //添加ConsumerChannel
            if (!Server.containConsumerChannnel(ctx.channel())) {
                Server.addConsumerChannel(ctx.channel());
            }
            response.addRpcService(rpcRegister.getRpcServiceMap());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(msg.getCommandType(),msg.getMessageType(),response,msg.getSeq()));
    }
}
