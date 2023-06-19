package com.rpc.lrpc.net;

/**
 * 接受响应时执行的操作由不同的Handler执行，主要作用是检测请求的响应是否到达
 */
public class ResponseAction {

    public boolean hasThreadWaiting()
    {
        return false;
    }
    public void action(){
        //NOOP
    }
}
