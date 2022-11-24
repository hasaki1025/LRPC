package com.rpc.lrpc.Context;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.Content.Request.DefaultPushServicesRequest;
import com.rpc.lrpc.message.Content.Request.PushServicesRequest;
import com.rpc.lrpc.message.RequestMessage;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import com.rpc.lrpc.net.DokiDokiMap;
import com.rpc.lrpc.net.Server;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
@Component
public class RpcRegisterContext implements RpcRegister {
     final Map<RpcService, RpcAddress[]> rpcServiceMap=new ConcurrentHashMap<>();
     final Map<String,RpcService> serviceNameMap=new ConcurrentHashMap<>();

     final Set<RpcAddress> addressSet=new CopyOnWriteArraySet<>();

     @Value("${RPC.Register.port}")
      Integer port;

     @Autowired
     DokiDokiMap dokiDokiMap;



    @Override
    public void registerService(RpcService service, RpcAddress rpcAddress) {
        if (rpcServiceMap.containsKey(service))
        {
            ArrayList<RpcAddress> urls = new ArrayList<>(List.of(rpcServiceMap.get(service)));
            urls.add(rpcAddress);
            rpcServiceMap.put(service,urls.toArray(new RpcAddress[0]));
        }
        else {
            rpcServiceMap.put(service,new RpcAddress[]{rpcAddress});
            serviceNameMap.put(service.getServiceName(),service);
        }
        dokiDokiMap.addUrl(rpcAddress);
        addressSet.add(rpcAddress);
        //发送广播
        DefaultPushServicesRequest request = new DefaultPushServicesRequest();
        request.setRpcService(service);
        request.setRpcAddress(rpcAddress);
        Server.broadcastMessage(new RequestMessage<PushServicesRequest>(CommandType.Push, MessageType.request,request));
    }

    @Override
    public RpcService getService(String serviceName) {
        return serviceNameMap.get(serviceName);
    }

    @Override
    public RpcAddress[] getRpcUrls(RpcService rpcService) {
        return rpcServiceMap.get(rpcService);
    }

    @Override
    public RpcAddress[] getRpcUrlsByName(String serviceName) {
        return rpcServiceMap.get(serviceNameMap.get(serviceName));
    }

    @Override
    public RpcAddress[] getAllUrl() {
        return addressSet.toArray(new RpcAddress[0]);
    }

    @Override
    public void removeAddress(RpcAddress rpcAddress) {
        if (!addressSet.remove(rpcAddress)) {
            throw new RuntimeException("Do not have this rpcaddress");
        }

        String serviceName = rpcAddress.getServiceName();
        if (serviceName==null || serviceName.equals(""))
        {
            throw new RuntimeException("Do not have this rpcaddress");
        }
        RpcService service = serviceNameMap.get(serviceName);
        RpcAddress[] addresses = rpcServiceMap.get(service);
        if (addresses.length==1 && addresses[0].equals(rpcAddress)) {
            rpcServiceMap.remove(service);
            serviceNameMap.remove(serviceName);
        }else {
            List<RpcAddress> list = List.of(addresses);
            list.remove(rpcAddress);
            rpcServiceMap.put(service,list.toArray(new RpcAddress[0]));
        }
        DefaultPushServicesRequest request = new DefaultPushServicesRequest();
        request.setRpcAddress(rpcAddress);
        request.setRpcService(service);
        Server.broadcastMessage(new RequestMessage<PushServicesRequest>(CommandType.Push,MessageType.request,request));
    }

    @Override
    public void removeAddress(RpcAddress rpcAddress) {
        if (!addressSet.remove(rpcAddress)) {
            throw new RuntimeException("Do not have this rpcaddress");
        }

        String serviceName = rpcAddress.getServiceName();
        if (serviceName==null || serviceName.equals(""))
        {
            throw new RuntimeException("Do not have this rpcaddress");
        }
        RpcService service = serviceNameMap.get(serviceName);
        RpcAddress[] addresses = rpcServiceMap.get(service);
        if (addresses.length==1 && addresses[0].equals(rpcAddress)) {
            rpcServiceMap.remove(service);
            serviceNameMap.remove(serviceName);
        }else {

        }

    }


}
