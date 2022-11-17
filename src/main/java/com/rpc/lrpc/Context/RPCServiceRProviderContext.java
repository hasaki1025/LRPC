package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;


@Data
public class RPCServiceRProviderContext implements RPCServiceProvider{
    @Value("${RPC.Provider.ServiceName:null}")
    String serviceName;
    @Value("${RPC.Provider.port:null}")
    Integer port;
    @Value("${RPC.Server.Host}")
    String registerServerHost;
    @Value("${RPC.Server.port}")
    Integer registerServerPort;

    HashSet<RpcMapping> mappings=new HashSet<>();
    private RpcService rpcService;

    private final HashSet<RpcController> rpcControllers=new HashSet<>();


    public RpcService getRpcService()
    {
        //保证要在容器初始化完成后再调用getRpcService
       if (rpcService==null)
       {
           rpcService=new RpcService(serviceName,rpcControllers.toArray(new RpcController[0]));
       }
       return rpcService;
    }

    @Override
    //TODO 向注册中心注册服务
    public void registerService() {

    }

    @Override
    //TODO 向Server发送心跳
    public void dokidoki() {

    }

}
