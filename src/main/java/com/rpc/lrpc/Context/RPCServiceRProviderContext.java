package com.rpc.lrpc.Context;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;


@Data
@ConditionalOnProperty(name={
        "RPC.Provider.ServiceName",
        "RPC.Provider.port",
        "RPC.Server.Host",
        "RPC.Server.port"}
)
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
    private RpcAddress rpcAddress;
    @Value("${RPC.Config.ChannelType}")
    String channelType;

    @Value("${RPC.Config.HeartGap}")
    int heartGap;

    final Set<RpcMapping> mappings=new CopyOnWriteArraySet<>();

    final Map<String,RpcMapping> mappingMap=new ConcurrentHashMap<>();
    private RpcService rpcService;

    private final Set<RpcController> rpcControllers=new CopyOnWriteArraySet<>();

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

            List<RpcMapping> rpcMappings = List.of(controller.getRpcMappings());

            Map<String, RpcMapping> rpcMappingMap =
                    rpcMappings.stream().collect(Collectors.toMap(RpcMapping::getMapping, Function.identity()));
            mappingMap.putAll(rpcMappingMap);

            mappings.addAll(rpcMappings);
            list.add(controller);
        }
        rpcControllers.addAll(list);
        rpcService=new RpcService(serviceName,rpcControllers.toArray(new RpcController[0]));
        try {
            rpcAddress =new RpcAddress(InetAddress.getLocalHost().getHostAddress(),port,serviceName);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMapping(Collection<RpcMapping> rpcMappings)
    {
        mappings.addAll(rpcMappings);
        Map<String, RpcMapping> map =
                rpcMappings.stream().collect(Collectors.toMap(RpcMapping::getMapping, Function.identity()));
        mappingMap.putAll(map);
    }

    @Override
    public RpcMapping getMapping(String mapping) {
        return mappingMap.get(mapping);
    }

    @Override
    public void addController(RpcController rpcController) {
        rpcControllers.add(rpcController);
    }

    @Override
    public RpcAddress getRpcUrl() {
        if (rpcAddress==null)
        {
            try {
                rpcAddress =new RpcAddress(InetAddress.getLocalHost().getHostAddress(),port,serviceName);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return rpcAddress;
    }

    @Override
    public Object invokeMapping(Object[] params, String mapping) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RpcMapping rpcMapping = mappingMap.get(mapping);
        Class<?> clazz = rpcMapping.getClazz();
        Method method = clazz.getDeclaredMethod(rpcMapping.getMethodName(), rpcMapping.getParamType());
        Object bean = applicationContext.getBean(clazz);
        return method.invoke(bean,params);
    }

    @Override
    public RpcMapping[] getMappings() {
        return mappings.toArray(new RpcMapping[0]);
    }
}
