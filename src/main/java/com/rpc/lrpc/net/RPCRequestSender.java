package com.rpc.lrpc.net;

import com.rpc.lrpc.Util.MessageUtil;
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

    public  Object callSync(String address,Object...params)
    {
        try {
            String[] strings = MessageUtil.parseAddress(address);
            String replace = address.replace(strings[2], "");
            ConsumerClient client = channelPool.getConnection(replace, ConsumerClient.class);
            return client.callSync(params, strings[2]);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public  <T>void call(String address, Consumer<T> consumer, Object...params)
    {
        try {
            String[] strings = MessageUtil.parseAddress(address);
            String replace = address.replace(strings[2], "");
            ConsumerClient client = channelPool.getConnection(replace, ConsumerClient.class);
            client.call(params, strings[2],consumer);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
