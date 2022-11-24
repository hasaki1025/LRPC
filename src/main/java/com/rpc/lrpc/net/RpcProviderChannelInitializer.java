package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RPCServiceProvider;
import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.RpcRole;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@ConditionalOnBean(RPCServiceProvider.class)
@Component
@Slf4j
public class RpcProviderChannelInitializer extends RpcChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        ch.attr(AttributeKey.valueOf("ClientRole")).set(RpcRole.Provider.getValue());
    }
}
