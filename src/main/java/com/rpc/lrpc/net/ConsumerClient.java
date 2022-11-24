package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcConsumer;
import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.*;
import com.rpc.lrpc.message.RequestMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

public class ConsumerClient extends Client{


    final RpcConsumer rpcConsumer;

    public ConsumerClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, ChannelInitializer<? extends Channel> channelInitializer,RpcConsumer rpcConsumer,ResponseMap responseMap) {
        super(group, workerGroup, channelInitializer,responseMap);
        this.rpcConsumer=rpcConsumer;
    }

    public void pullService()
    {
        channel.writeAndFlush(new RequestMessage<PullServicesRequest>(CommandType.Pull,getNextSeq(), MessageType.request,new DefaultPullServicesRequest()));
    }

    public void updateService(String serviceName)
    {
        DefaultUpdateServiceRequest request = new DefaultUpdateServiceRequest();
        request.setServiceName(serviceName);
        channel.writeAndFlush(
                new RequestMessage<UpdateServiceRequest>(CommandType.Update,getNextSeq(),MessageType.request,request));
    }

    public <T> T callSync(String mapping,Class<T> returnType,Object... params)
    {
        DefaultCallServicesRequest request = new DefaultCallServicesRequest();
        request.setMapping(mapping);
        request.setParamValues(params);
        int seq = getNextSeq();
        channel.writeAndFlush(new RequestMessage<CallServicesRequest>(CommandType.Call, seq, MessageType.request, request));
        synchronized (ResponseMap.WAITING_MAP.get(seq)) {
            try {
                //TODO 开始堵塞等待响应结果
                ResponseMap.WAITING_MAP.get(seq).wait(rpcConsumer.getRequestTimeOut());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ResponseMap map = getResponseMap();
        return (T) map.getResponse(seq);

    }
}
