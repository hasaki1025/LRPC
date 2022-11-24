package com.rpc.lrpc.Enums;

import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

public enum ChannelType {
    NIO(0),EPOLL(1);



    private static final Class<? extends Channel>[] ChannelClass=new Class[]{
            NioSocketChannel.class, EpollSocketChannel.class
    };

    private static final Map<String,ChannelType> map=new HashMap<>();

    static {
        map.put("NIO",ChannelType.NIO);
        map.put("EPOLL",ChannelType.EPOLL);
    }
    public static Class<? extends Channel> ToChannelClass(String i)
    {
        return ChannelClass[map.get(i).value];
    }
    final int value;
    ChannelType(int i) {
        this.value=i;
    }


}
