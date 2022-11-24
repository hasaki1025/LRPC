package com.rpc.lrpc.Handler;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DokiDokiRequest;
import com.rpc.lrpc.message.Content.Response.DokiDokiResponse;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.ResponseMessage;
import com.rpc.lrpc.message.RpcURL;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@ConditionalOnBean(RPCServiceProvider.class)
@Slf4j
@ChannelHandler.Sharable
@Order(3)
public class DokiDokiRequestHandler extends SimpleChannelInboundHandler<RequestMessage<DokiDokiRequest>> {

    @Value("${RPC.Provider.port}")
    int port;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage<DokiDokiRequest> msg) throws Exception {
        DokiDokiResponse response = new DokiDokiResponse();
        RpcURL url = new RpcURL();
        url.setHost( InetAddress.getLocalHost().getHostAddress());
        url.setPort(port);
        response.setRpcURL(url);
        ctx.writeAndFlush(new ResponseMessage<>(CommandType.DokiDoki, msg.getSeq(), MessageType.response, response));
    }
}
