package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

@ConditionalOnBean(ChannelPool.class)
@Component
public class RPCRequestSender {
    @Autowired
    ChannelPool channelPool;

    @Autowired
    RpcConsumer consumer;

    public  Object callSync(String url,Object...params)
    {
        try {
            RpcUrl rpcUrl = MessageUtil.parseUrl(url);
            if (!consumer.containAddress(rpcUrl.getAddress())) {
                throw new RuntimeException("Can not find addess of this service");
            }
            else {
                ConsumerClient client = channelPool.getConnection(rpcUrl.getAddress().toString(), ConsumerClient.class);
                return client.callSync(params, rpcUrl.getMapping());
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public  <T>void call(String url, Consumer<T> consumer, Object...params)
    {
        try {
            RpcUrl rpcUrl = MessageUtil.parseUrl(url);
            ConsumerClient client = channelPool.getConnection(rpcUrl.getAddress().toString(), ConsumerClient.class);
            client.call(params, rpcUrl.getMapping(),consumer);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
