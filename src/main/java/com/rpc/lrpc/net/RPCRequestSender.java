package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcUrl;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@ConditionalOnBean(ChannelPool.class)
@Component
public class RPCRequestSender {
    @Autowired
    ChannelPool channelPool;

    @Autowired
    RpcConsumer consumer;

    ConsumerClient registerChannel;

    private volatile boolean isInit=false;

    public void init() throws ExecutionException, InterruptedException {
        if (!isInit)
        {
            isInit=true;
            ConsumerClient client = channelPool.getConsumerConnection("rpc://" + consumer.getRegisterServerHost() + ":" + consumer.getRegisterServerPort());
            client.pull();
            registerChannel=client;
        }
    }

    public Optional<Object> callSync(String url, Object...params)
    {
        try {
            RpcUrl rpcUrl = MessageUtil.parseUrl(url);
            RpcAddress rpcAddress = consumer.getRpcAddress(rpcUrl.getAddress().getServiceName());
            ConsumerClient client = channelPool.getConsumerConnection(rpcAddress.toString());
            return client.callSync(params, rpcUrl.getMapping());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public  <T>void call(String url, Consumer<T> resultConsumer, Object...params)
    {
        try {
            RpcUrl rpcUrl = MessageUtil.parseUrl(url);
            RpcAddress rpcAddress = consumer.getRpcAddress(rpcUrl.getAddress().getServiceName());
            rpcUrl.setAddress(rpcAddress);
            ConsumerClient client = channelPool.getConsumerConnection(rpcUrl.getAddress().toString());
            client.call(params, rpcUrl.getMapping(),resultConsumer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
