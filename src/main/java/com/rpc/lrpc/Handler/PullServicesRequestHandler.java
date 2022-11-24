package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.*;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.Content.Response.PullServicesResponse;
import com.rpc.lrpc.net.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        PullServicesResponse response = new PullServicesResponse();
        try
        {
            //添加ConsumerChannel
            if (!Server.containConsumerChannnel(ctx.channel())) {
                Server.addConsumerChannel(ctx.channel());
            }
            HashMap<String, RpcAddress[]> addressMap = new HashMap<>();
            HashMap<String, String[]> mappingMap = new HashMap<>();
            for (String serviceName : rpcRegister.getAllServiceName()) {
                RpcAddress[] address = rpcRegister.getRpcAddress(serviceName);
                addressMap.put(serviceName,address);
                mappingMap.put(serviceName,rpcRegister.getMappings(serviceName));
            }
            response.setAddressMap(addressMap);
            response.setMappingMap(mappingMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(msg.getCommandType(),MessageType.response,response,msg.getSeq()));
    }
}
