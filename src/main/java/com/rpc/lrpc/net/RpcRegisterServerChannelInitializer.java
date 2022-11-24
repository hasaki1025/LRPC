package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.RpcRole;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Channel channel = ctx.channel();

    }
}
