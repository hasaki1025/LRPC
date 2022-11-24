package com.rpc.lrpc.net;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.MessageContent;
import com.rpc.lrpc.message.Content.Request.DefaultPushServicesRequest;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Request.RegisterRequest;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class Server implements Closeable {

    @Autowired
    @Qualifier("group")
    EventLoopGroup bossGroup;
    @Autowired
    @Qualifier("chirdGroup")
    EventLoopGroup chirdGroup;
    @Autowired
    @Qualifier("workerGroup")
    DefaultEventLoopGroup workerGroup;
    ServerChannel serverChannel;


    private final static ChannelGroup CHANNEL_CONSUMER_GROUP =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static boolean containConsumerChannnel(Channel channel)
    {
        return CHANNEL_CONSUMER_GROUP.contains(channel);
    }

    public static void addConsumerChannel(Channel channel)
    {
        CHANNEL_CONSUMER_GROUP.add(channel);
    }

    public  static void broadcastMessage(Message message)
    {
        CHANNEL_CONSUMER_GROUP.writeAndFlush(message);
    }


    void init(int port,RpcServerChannelInitializer channelInitializer)
    {
        try {

            new ServerBootstrap()
                    .group(bossGroup,chirdGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .bind(port).addListener((ChannelFutureListener) future -> {
                        if(future.channel() instanceof NioServerSocketChannel)
                        {
                            serverChannel= (NioServerSocketChannel) future.channel();
                        }
                        serverChannelInit();
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void serverChannelInit() {
        //NOOP
    }

    @Override
    public void close() throws IOException {
        try {
            serverChannel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
