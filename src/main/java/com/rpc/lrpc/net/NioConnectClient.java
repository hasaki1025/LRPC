package com.rpc.lrpc.net;

import com.rpc.lrpc.Util.MessageUtil;
import com.rpc.lrpc.message.RpcURL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({NioEventLoopGroup.class, DefaultEventLoopGroup.class})
public class NioConnectClient extends ConnectClient {

    @Autowired
    NioEventLoopGroup group;
    @Autowired
    DefaultEventLoopGroup workerGroup;
    Channel channel;
    @Autowired
    List<ChannelHandler> handlersChain;

    @Override
    public void init(String address) {
        RpcURL url = MessageUtil.getUrlByString(address);
        try {
            channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
                        }
                    }).connect(url.getHost(),url.getPort()).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
