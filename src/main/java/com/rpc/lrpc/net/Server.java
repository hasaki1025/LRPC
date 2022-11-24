package com.rpc.lrpc.net;

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
import org.springframework.beans.factory.annotation.Value;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class Server implements Closeable {

    @Value("${RPC.Register.port}")
    int port;
    EventLoopGroup bossGroup;
    EventLoopGroup chirdGroup;
    DefaultEventLoopGroup workerGrop;
    ServerChannel serverChannel;
    @Autowired
    List<ChannelHandler> handlersChain;

    //用于保存所有连接
    private final ChannelGroup channelGroup =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    void NIOServerInit()
    {
        try {
            bossGroup=new NioEventLoopGroup(1);
            chirdGroup=new NioEventLoopGroup();
            workerGrop=new DefaultEventLoopGroup();
            new ServerBootstrap()
                    .group(bossGroup,chirdGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            channelGroup.add(ctx.channel());
                        }
                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                            channelGroup.remove(ctx.channel());
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
        finally {
            bossGroup.shutdownGracefully();
            chirdGroup.shutdownGracefully();
            workerGrop.shutdownGracefully();
        }
    }
}
