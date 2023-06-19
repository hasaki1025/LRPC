package com.rpc.lrpc.net;

import com.rpc.lrpc.Util.ConditionServer;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * server信道初始化类
 */
@Slf4j
public class RpcServerChannelInitializer extends ChannelInitializer<Channel> {


    List<ChannelHandler> handlersChain;




    public RpcServerChannelInitializer(List<ChannelHandler> handlersChain) {
        this.handlersChain = handlersChain;
    }

    /**
     * 初始化server信道
     * @param ch 信道餐宿
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
        log.info("Server Connect to {}",ch.remoteAddress());
    }
}
