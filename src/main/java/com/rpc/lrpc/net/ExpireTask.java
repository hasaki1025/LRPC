package com.rpc.lrpc.net;

import com.rpc.lrpc.Util.MessageUtil;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听某一请求是否过期的延时任务，通过延时任务线程池提交，到点后如果该请求仍未受到响应则从map中删除该请求并打印错误日志
 */
@Slf4j
public class ExpireTask implements Runnable{


    /**
     * 监听的信道
     */
    Channel channel;

    /**
     * 监听的请求
     */
    int seq;

    public ExpireTask(Channel channel, int seq) {
        this.channel = channel;
        this.seq = seq;
    }

    /**
     * 获取该信道的响应Map
     * @return ChannelResponse
     */
    public ChannelResponse getChannelResponse()
    {
        return (ChannelResponse) channel.attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
    }


    /**
     * 检测该任务绑定的请求是否已经接受到响应，如果没有视该请求已失败
     */
    @Override
    public void run() {
        ChannelResponse response = getChannelResponse();
        if (response.stillWaiting(seq)) {
            log.error("{} request expire....",seq);
            response.removeResponseAction(seq);
        }
    }
}
