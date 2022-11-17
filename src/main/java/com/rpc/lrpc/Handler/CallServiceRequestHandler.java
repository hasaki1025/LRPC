package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.message.Content.Request.CallServicesRequest;
import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.DefaultCallServicesResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcMapping;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RequestMessage<CallServicesRequest>> {

    @Autowired
    RPCServiceProvider provider;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<CallServicesRequest> msg) {

        DefaultCallServicesResponse response = new DefaultCallServicesResponse();
        try {
            log.info("get Call ServicesRequest:{}",msg);
            CallServicesRequest request = msg.content();
            //TODO 在此处获取Mapping对应的方法和参数等信息
            RpcMapping mapping = provider.getMapping(request.getMapping());
            Class<?> clazz = mapping.getClazz();
            Object bean = applicationContext.getBean(clazz);
            Object result = mapping.getSource().invoke(bean, request.getParamValues());
            log.info("Method Invoke successfully....");
            response.setResult(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("Method Invoke fail....");
            response.setException(e);
            e.printStackTrace();
        }
        new ResponseMessage<CallServicesResponse>()
        ctx.writeAndFlush(response);
    }
}
