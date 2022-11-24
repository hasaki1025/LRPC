package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Context.RpcRegister;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.message.Content.Request.UpdateServiceRequest;
import com.rpc.lrpc.message.Content.Response.DefaultUpdateServiceResponse;
import com.rpc.lrpc.message.Content.Response.UpdateServiceResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@ConditionalOnBean(RpcRegister.class)
@Slf4j
@ChannelHandler.Sharable
public class UpdateServiceRequestHandler extends SimpleChannelInboundHandler<RequestMessage<UpdateServiceRequest>> {

    @Autowired
    RpcRegister rpcRegister;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<UpdateServiceRequest> msg) throws Exception {
        UpdateServiceResponse response = new DefaultUpdateServiceResponse();
        try{
            String serviceName = msg.content().getServiceName();
            RpcService service = rpcRegister.getService(serviceName);
            response.setRpcService(service);
            response.setRpcUrls(rpcRegister.getRpcUrlsByName(serviceName));
        }catch (Exception e)
        {
            e.printStackTrace();
            response.setException(e);
        }
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.Update, msg.getSeq(), MessageType.response, response, RpcRole.Register));
    }
}
