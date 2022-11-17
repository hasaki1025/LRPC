package com.rpc.lrpc.Context;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;


@Data
@ConditionalOnProperty(name={"RPC.Provider.ServiceName","RPC.Provider.port","RPC.Server.Host","RPC.Server.port"})
@Component
public class RPCServiceRProviderContext implements RPCServiceProvider{
    @Value("${RPC.Provider.ServiceName}")
     String serviceName;
    @Value("${RPC.Provider.port}")
     int port;
    @Value("${RPC.Server.Host}")
     String registerServerHost;
    @Value("${RPC.Server.port}")
     int registerServerPort;
    @Autowired
    ConfigurableApplicationContext applicationContext;

    final HashSet<RpcMapping> mappings=new HashSet<>();
    private RpcService rpcService;

    private final HashSet<RpcController> rpcControllers=new HashSet<>();

    @Override
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
    public void init()
    {
        ArrayList<RpcController> list = new ArrayList<>();
        for (String s : applicationContext.getBeanNamesForAnnotation(RPCController.class)) {
            Object bean = applicationContext.getBean(s);
            RpcController controller = new RpcController(bean.getClass(), serviceName, s);
            mappings.addAll(List.of(controller.getRpcMappings()));
            list.add(controller);
        }
        rpcControllers.addAll(list);
        rpcService=new RpcService(serviceName,rpcControllers.toArray(new RpcController[0]));
    }

    @Override
    public void addMapping(Collection<RpcMapping> rpcMappings)
    {
        mappings.addAll(rpcMappings);
    }
    @Override
    public RpcMapping[] getMappings() {
        return mappings.toArray(new RpcMapping[0]);
    }
}
