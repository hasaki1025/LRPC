package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@Component
@ConditionalOnBean(RpcConsumer.class)
public class ConsumerChannelPool {

    //连接池，key为连接地址，value是连接
    private volatile ConcurrentHashMap<String, ConsumerToProviderClient> connectionPool;

    //连接锁，防止connection重复创建
    private volatile ConcurrentHashMap<String,Object> connectionLocks=new ConcurrentHashMap<>();

    @Value("${RPC.Config.ChannelType:NIO}")
    String channelType;

    @Autowired
    @Qualifier("group")
    EventLoopGroup group;

    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup defaultEventLoopGroup;

    @Autowired
    RpcConsumer rpcConsumer;
    @Value("${RPC.Config.RequestTimeOut}")
    long requestTimeOut;
    @Autowired
    List<ChannelHandler> handlers;

    //TODO clientImpl的class需要设置吗
    public ConsumerToProviderClient getConsumerConnection(String address) {
        if (connectionPool==null) {
            synchronized (this) {
                if (connectionPool==null) {
                    connectionPool=new ConcurrentHashMap<>();
                }
            }
        }

        ConsumerToProviderClient connection = connectionPool.get(address);
        if (connection!=null) {
            return connection;
        }

        Object lock = connectionLocks.get(address);
        if (lock==null) {
            connectionLocks.putIfAbsent(address,new Object());
        }
        synchronized (connectionLocks.get(address)) {
            ConsumerToProviderClient client = connectionPool.get(address);
            if (client!=null) {
                return client;
            }
            ConsumerToProviderClient consumerToProviderClient = new ConsumerToProviderClient(group, defaultEventLoopGroup, handlers, requestTimeOut);
            //连接初始化
            RpcAddress url = MessageUtil.parseAddress(address);
            consumerToProviderClient.init(url.getHost(),url.getPort(), ChannelType.ToChannelClass(channelType));
            connectionPool.put(address, consumerToProviderClient);
        }
        return  connectionPool.get(address);
    }

}
