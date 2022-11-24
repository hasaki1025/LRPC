package com.rpc.lrpc.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Client implements Closeable {
    Channel channel;
    NioEventLoopGroup group;
    DefaultEventLoopGroup workerGroup;

    final AtomicInteger seqCounter=new AtomicInteger();

    @Autowired
    List<ChannelHandler> handlersChain;




    //魔数（4）-版本号（1）-序列化算法（1）-消息类型（1）-指令类型(1)-请求序号(4)-正文长度(4)-消息本体,最大长度
    void init(String host,int port)
    {
        group=new NioEventLoopGroup();
        workerGroup=new DefaultEventLoopGroup();
            new Bootstrap()
                .group(group)
                .channel(io.netty.channel.socket.nio.NioSocketChannel.class)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
                    }
                }).connect(host, port).addListener((ChannelFutureListener) future -> {
                    channel=future.channel();
                    channelInit();
                });
        //此时还没有获取到Channel，对于channel建立时需要的操作需要由子类实现
    }

    @Override
    public void close() throws IOException {
        SocketAddress remoteAddress = channel.remoteAddress();
        ChannelFuture close = channel.close();
        close.addListener((ChannelFutureListener) future -> {
            log.info("disconnnect...{}", remoteAddress);
            group.shutdownGracefully();
            workerGroup.shutdownGracefully();
        });
    }

    int getNextSeq()
    {
        return seqCounter.getAndIncrement();
    }

    //在此定义channel建立后需要就行的操作
    void channelInit()
    {

    }

}
