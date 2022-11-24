package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
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
            RpcAddress rpcAddress = MessageUtil.parseAddress(address);
            if (!consumer.containAddress(rpcAddress)) {

            }
            ConsumerClient client = channelPool.getConnection(address, ConsumerClient.class);
            return client.callSync(params, rpcAddress.getMapping());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public  <T>void call(String address, Consumer<T> consumer, Object...params)
    {
        try {
            RpcAddress rpcAddress = MessageUtil.parseAddress(address);
            ConsumerClient client = channelPool.getConnection(address, ConsumerClient.class);
            client.call(params, rpcAddress.getMapping(),consumer);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
