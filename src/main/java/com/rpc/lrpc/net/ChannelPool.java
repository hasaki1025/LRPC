package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.ChannelType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
public class ChannelPool {

    //连接池，key为连接地址，value是连接
    private volatile ConcurrentHashMap<String,Client> connectionPool;

    //连接锁，防止connection重复创建
    private volatile ConcurrentHashMap<String,Object> connectionLocks=new ConcurrentHashMap<>();

    @Value("${Rpc.Config.ChannelType}")
    String channelType;

    @Autowired
    @Qualifier("group")
    EventLoopGroup group;

    @Autowired
    ChannelInitializer<?> channelInitializer;
    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup defaultEventLoopGroup;

    public  Client getConnection(String address,Class<? extends Client> clientImpl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (connectionPool==null) {
            synchronized (this) {
                if (connectionPool==null) {
                    connectionPool=new ConcurrentHashMap<>();
                }
            }
        }

        Client connection = connectionPool.get(address);
        if (connection!=null) {
            return connection;
        }

        Object lock = connectionLocks.get(address);
        if (lock==null) {
            connectionLocks.putIfAbsent(address,new Object());
            lock=connectionLocks.get(address);
        }
        synchronized (lock) {
            Client client = connectionPool.get(address);
            if (client!=null) {
                return client;
            }

            Client instance = clientImpl
                    .getDeclaredConstructor(EventLoopGroup.class, DefaultEventLoopGroup.class, ChannelInitializer.class)
                    .newInstance(group,defaultEventLoopGroup,channelInitializer);
            //连接初始化
            String[] split = address.split(":");
            instance.init(split[0],Integer.parseInt(split[1]), ChannelType.ToChannelClass(channelType));
            connectionPool.put(address,instance);
        }
        return connectionPool.get(address);
    }

}
