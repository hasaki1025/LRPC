package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Enums.RpcRole;
import com.rpc.lrpc.Enums.SerializableType;
import com.rpc.lrpc.message.Content.Request.CallServicesRequest;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcMapping;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
@Order(3)
@ChannelHandler.Sharable
@ConditionalOnBean(RPCServiceProvider.class)
public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RequestMessage<CallServicesRequest>> {

    @Autowired
    RPCServiceProvider provider;


    @Value("${RPC.Config.SerializableType}")
    String serializableType;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<CallServicesRequest> msg) {
        DefaultCallServicesResponse response = new DefaultCallServicesResponse();
        //TODO 在此处获取Mapping对应的方法和参数等信息
        try {
            log.info("get Call ServicesRequest:{}",msg);
            CallServicesRequest request = msg.content();
            //TODO 同步update请求服务列表并再次发起Call请求
            response.setResult(provider.invokeMapping(request.getParamValues(),request.getMapping()));
            log.info("Method Invoke successfully....");
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //并没有定义size
        ctx.writeAndFlush(
                new ResponseMessage<CallServicesResponse>(CommandType.Call,MessageType.response,response,msg.getSeq())
        );
    }
}
