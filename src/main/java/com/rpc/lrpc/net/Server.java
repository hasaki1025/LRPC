package com.rpc.lrpc.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.*;
import com.rpc.lrpc.message.Content.Broadcast.BroadMassageContent;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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


    public final static ChannelGroup CHANNEL_CONSUMER_GROUP=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static boolean containConsumerChannnel(Channel channel)
    {
        return CHANNEL_CONSUMER_GROUP.contains(channel);
    }

    public static void addConsumerChannel(Channel channel)
    {
        CHANNEL_CONSUMER_GROUP.add(channel);
    }

    public  static void broadcastMessage(BroadcastMessage<? extends BroadMassageContent> message)
    {
        CHANNEL_CONSUMER_GROUP.forEach(channel -> {
            try {
                ByteBuf buffer = channel.alloc().buffer();
                MessageUtil.broadcastMessageToBuffer(message, buffer);
                channel.writeAndFlush(buffer).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("send BroadCast Messsage to {}",channel.remoteAddress());
                    }else if (future.isCancelled())
                    {
                        log.error("send BroadCast Messsage to {} canceled",channel.remoteAddress());
                    }else {
                        log.error("send BroadCast Messsage to {} fail ",channel.remoteAddress());
                        log.error("ERROR :  ",future.cause());
                    }
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }


    void init(int port,RpcServerChannelInitializer channelInitializer)
    {
        try {
            new ServerBootstrap()
                    .group(bossGroup,chirdGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .bind(port).addListener((ChannelFutureListener) future -> {
                        log.info("Server [{}] start....",future.channel().localAddress());
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

    protected void serverChannelInit() {
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
