package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcURL;
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
    private volatile ConcurrentHashMap<String,Client> connectionPool;

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
    public<T> T getConnection(String address,Class<T> clientImpl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (connectionPool==null) {
            synchronized (this) {
                if (connectionPool==null) {
                    connectionPool=new ConcurrentHashMap<>();
                }
            }
        }

        Client connection = connectionPool.get(address);
        if (connection!=null) {
            return (T) connection;
        }

        Object lock = connectionLocks.get(address);
        if (lock==null) {
            connectionLocks.putIfAbsent(address,new Object());
            lock=connectionLocks.get(address);
        }
        synchronized (lock) {
            Client client = connectionPool.get(address);
            if (client!=null) {
                return (T) client;
            }

            if (channelInitializer==null) {
                channelInitializer=new RpcClientChannelInitializer(handlers);
            }
            Client instance = (Client) clientImpl
                    .getDeclaredConstructor(EventLoopGroup.class, DefaultEventLoopGroup.class, ChannelInitializer.class,RpcConsumer.class,ResponseMap.class)
                    .newInstance(group,defaultEventLoopGroup,channelInitializer,rpcConsumer,responseMap);
            //连接初始化
            RpcURL url = MessageUtil.getUrlByString(address);
            instance.init(url.getHost(),url.getPort(), ChannelType.ToChannelClass(channelType));
            connectionPool.put(address,instance);
        }
        return (T) connectionPool.get(address);
    }

}
