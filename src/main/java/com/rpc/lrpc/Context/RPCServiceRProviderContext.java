package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.HashSet;


@Data
public class RPCServiceRProviderContext implements RPCServiceProvider{
    @Value("${RPC.Provider.ServiceName}")
    final String serviceName;
    @Value("${RPC.Provider.port}")
    final int port;
    @Value("${RPC.Server.Host}")
    final String registerServerHost;
    @Value("${RPC.Server.port}")
    final int registerServerPort;

    final HashSet<RpcMapping> mappings=new HashSet<>();
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

}
