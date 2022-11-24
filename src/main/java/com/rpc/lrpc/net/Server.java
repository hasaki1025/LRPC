package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.MessageContent;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.Content.Request.RequestContent;
import com.rpc.lrpc.message.Message;
import com.rpc.lrpc.message.RequestMessage;
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
    @Autowired
    List<ChannelHandler> handlersChain;

    //用于保存所有连接
    private final static ChannelGroup CHANNEL_GROUP =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final static ChannelGroup CHANNEL_CONSUMER_GROUP =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public  boolean containConsumerChannnel(Channel channel)
    {
        return CHANNEL_CONSUMER_GROUP.contains(channel);
    }

    public  void addConsumerChannel(Channel channel)
    {
        CHANNEL_CONSUMER_GROUP.add(channel);
    }

    public  void broadcastMessage(MessageContent content)
    {
        //TODO 广播消息
    }


    void init(int port)
    {
        try {

            new ServerBootstrap()
                    .group(bossGroup,chirdGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            CHANNEL_GROUP.add(ctx.channel());
                        }
                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                            CHANNEL_GROUP.remove(ctx.channel());
                        }
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
                        }
                    }).bind(port).addListener((ChannelFutureListener) future -> {
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
        }finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
