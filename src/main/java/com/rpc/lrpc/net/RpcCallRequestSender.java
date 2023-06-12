package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.message.RpcUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Component
@ConditionalOnBean(RpcConsumer.class)
public class RpcCallRequestSender {

    @Autowired
    ConsumerChannelPool pool;

    @Autowired
    RpcConsumer rpcConsumer;
    @Autowired
    ConsumerToRegisterClient client;


    /**
     *
     * @param url,基本格式为rpc://服务名称:mapping
     * @param resultType 设置返回类型
     * @param params 请求参数
     * @return 返回远程调用的结果
     * @param <T> 根据resultType确定
     */
    public <T> Optional<T> callSync(String url,Class<T> resultType,Object...params) {
        RpcUrl rpcUrl = MessageUtil.parseUrl(url);
        String serviceName = rpcUrl.getAddress().getServiceName();
        String mapping = rpcUrl.getMapping();
        RpcAddress rpcAddress = rpcConsumer.getRpcAddress(serviceName);
        if (rpcAddress == null) {
            try {
                client.pull();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            rpcAddress = rpcConsumer.getRpcAddress(serviceName);
        }
        if (rpcAddress == null)
        {
            throw new RuntimeException("Call Service["+serviceName+"] Request fail..");
        }
        ConsumerToProviderClient connection = pool.getConsumerConnection(rpcAddress.toString());
        try {
            Optional<Object> result = connection.callSync(params, mapping);
            if (result.isPresent() && resultType.isAssignableFrom(result.get().getClass()))
            {
                return Optional.of((T) result.get());
            }
            else {
                return Optional.empty();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public<T> void call(String url, Consumer<T> consumer, Object...params) {
        RpcUrl rpcUrl = MessageUtil.parseUrl(url);
        String serviceName = rpcUrl.getAddress().getServiceName();
        String mapping = rpcUrl.getMapping();
        RpcAddress rpcAddress = rpcConsumer.getRpcAddress(serviceName);
        if (rpcAddress == null) {
            try {
                client.pull();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            rpcAddress = rpcConsumer.getRpcAddress(serviceName);
        }
        if (rpcAddress == null)
        {
            throw new RuntimeException("Call Service["+serviceName+"] Request fail..");
        }
        ConsumerToProviderClient connection = pool.getConsumerConnection(rpcAddress.toString());
        connection.call(params, mapping,consumer);
    }
}
