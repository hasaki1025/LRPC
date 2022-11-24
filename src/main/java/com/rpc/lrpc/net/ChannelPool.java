package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@Component
@ConditionalOnBean(RpcConsumer.class)
public class ChannelPool {

    //连接池，key为连接地址，value是连接
    private volatile ConcurrentHashMap<String,ConsumerClient> connectionPool;

    //连接锁，防止connection重复创建
    private volatile ConcurrentHashMap<String,Object> connectionLocks=new ConcurrentHashMap<>();

    @Value("${RPC.Config.ChannelType:NIO}")
    String channelType;

    @Autowired
    @Qualifier("group")
    EventLoopGroup group;

    ChannelInitializer<?> channelInitializer;
    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup defaultEventLoopGroup;

    @Autowired
    RpcConsumer rpcConsumer;
    @Autowired
    ResponseMap responseMap;
    @Autowired
    List<ChannelHandler> handlers;

    //TODO clientImpl的class需要设置吗
    public ConsumerClient getConsumerConnection(String address) {
        if (connectionPool==null) {
            synchronized (this) {
                if (connectionPool==null) {
                    connectionPool=new ConcurrentHashMap<>();
                }
            }
        }

        ConsumerClient connection = connectionPool.get(address);
        if (connection!=null) {
            return connection;
        }

        Object lock = connectionLocks.get(address);
        if (lock==null) {
            connectionLocks.putIfAbsent(address,new Object());
        }
        synchronized (connectionLocks.get(address)) {
            ConsumerClient client = connectionPool.get(address);
            if (client!=null) {
                return client;
            }

            if (channelInitializer==null) {
                channelInitializer=new RpcClientChannelInitializer(handlers);
            }
            ConsumerClient consumerClient = new ConsumerClient(group, defaultEventLoopGroup, handlers, responseMap);
            //连接初始化
            RpcAddress url = MessageUtil.parseAddress(address);
            consumerClient.init(url.getHost(),url.getPort(), ChannelType.ToChannelClass(channelType));
            connectionPool.put(address,consumerClient);
        }
        return  connectionPool.get(address);
    }

}
