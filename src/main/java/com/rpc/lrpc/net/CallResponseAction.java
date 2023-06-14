package com.rpc.lrpc.net;

import com.rpc.lrpc.message.Content.Response.CallServicesResponse;
import com.rpc.lrpc.message.Content.Response.ResponseContent;

import java.util.function.Consumer;

/**
 * 服务调用返回时执行action方法，
 * @param <T> 成员Consumer接受的参数类型
 */
public class CallResponseAction<T> extends ResponseAction {

    private Consumer<T> consumer;

    private ResponseContent content;

    @Override
    public boolean hasThreadWaiting() {
        return consumer==null;
    }

    public void setContent(ResponseContent content) {
        this.content = content;
    }

    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }


    @Override
    public void action() {

        if (consumer!=null)
        {
            CallServicesResponse response = (CallServicesResponse) content;
            T result = (T) response.getResult();
            consumer.accept(result);
        }
        super.action();
    }
}
