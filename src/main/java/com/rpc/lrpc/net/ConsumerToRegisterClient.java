package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.ChannelType;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.PullServicesRequest;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 消费者对注册执行的Client
 */
@Component
@ConditionalOnBean(RpcConsumer.class)
@Slf4j
public class ConsumerToRegisterClient extends Client{

    /**
     * 是否已初始化，原子类型
     */
    private volatile boolean isInit=false;
    @Autowired
    RpcConsumer rpcConsumer;
    @Autowired
    LoggingHandler loggingHandler;

    /**
     * 初始化，启动时调用pull方法进行服务发现
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 中断异常
     */
    public void init() throws ExecutionException, InterruptedException {
        if (!isInit)
        {
            isInit=true;
            super.init(rpcConsumer.getRegisterServerHost(), rpcConsumer.getRegisterServerPort(), ChannelType.ToChannelClass(rpcConsumer.getChannelType()));
            pull();
        }
    }


    @Autowired
    public ConsumerToRegisterClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers, @Value("${RPC.Config.RequestTimeOut}") long timeout) {
        super(group, workerGroup, handlers, timeout);
    }

    /**
     * 服务发现方法，向指定注册中心发送pull报文
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 中断异常
     */
    public void pull() throws ExecutionException, InterruptedException {
        PullServicesRequest request = new PullServicesRequest();
        RequestMessage<PullServicesRequest> message = new RequestMessage<>(CommandType.Pull, MessageType.request, request);
        sendMessageAsyn(message,new ResponseAction(){
            @Override
            public void action() {
                log.info("get pull response");
            }
        });
    }








}
