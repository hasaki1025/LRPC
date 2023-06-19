package com.rpc.lrpc.net;

import com.rpc.lrpc.Context.RpcRegister;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;

import java.util.List;

/**
 * 注册中心信道处理器
 */
public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {


    /**
     * 心跳检测Map
     */
    DokiDokiMap dokiDokiMap;

    /**
     * 工作线程池
     */
    DefaultEventLoopGroup workerGroup;


    /**
     * 心跳检测间隔
     */
    int heartCheckTime;


    /**
     * 注册中心上下文
     */
    RpcRegister register;


    /**
     * 初始化信道
     * @param handlersChain handler链
     * @param dokiDokiMap 心跳Map
     * @param workerGroup 工作线程池
     * @param heartCheckTime 心跳检测间隔
     * @param register 注册中心
     */
    public RpcRegisterServerChannelInitializer(List<ChannelHandler> handlersChain, DokiDokiMap dokiDokiMap, DefaultEventLoopGroup workerGroup, int heartCheckTime, RpcRegister register) {
        super(handlersChain);
        this.dokiDokiMap = dokiDokiMap;
        this.workerGroup = workerGroup;
        this.heartCheckTime = heartCheckTime;
        this.register = register;
    }



}
