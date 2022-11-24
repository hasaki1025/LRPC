package com.rpc.lrpc.Context;

import com.rpc.lrpc.Enums.CommandType;
import com.rpc.lrpc.Enums.MessageType;
import com.rpc.lrpc.message.BroadcastMessage;
import com.rpc.lrpc.message.Content.Broadcast.DeleteContent;
import com.rpc.lrpc.message.Content.Broadcast.PushContent;
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

@Data
@ConditionalOnProperty(name = {
        "RPC.Register.port"
})
@Component
public class RpcRegisterContext implements RpcRegister {


    /**
     * key为服务名称，value为mapping名称
     */
     final Map<String,String[]> mappingMap=new ConcurrentHashMap<>();
    /**
     * key为服务名称，value为该服务所有地址
     */
    final Map<String,RpcAddress[]> addressMap=new ConcurrentHashMap<>();



     @Value("${RPC.Register.port}")
      Integer port;

     @Autowired
     DokiDokiMap dokiDokiMap;



    @Override
    public void registerService(String[] mappings, RpcAddress rpcAddress) {
        Set<String> serviceSet = addressMap.keySet();
        String serviceName = rpcAddress.getServiceName();
        if (serviceSet.contains(serviceName))
        {
            ArrayList<RpcAddress> urls = new ArrayList<>(List.of(addressMap.get(serviceName)));
            urls.add(rpcAddress);
            mappingMap.put(serviceName,mappings);
            addressMap.put(serviceName,urls.toArray(new RpcAddress[0]));
        }
        else {
            addressMap.put(serviceName,new RpcAddress[]{rpcAddress});
            mappingMap.put(serviceName,mappings);
        }
        dokiDokiMap.addUrl(rpcAddress);
        PushContent content = new PushContent();
        content.setMappings(mappings);
        content.setRpcAddress(rpcAddress);
        Server.broadcastMessage(new BroadcastMessage<>(CommandType.Push, MessageType.broadcast, content));
    }

    @Override
    public RpcAddress[] getRpcAddress(String serviceName) {
        return addressMap.get(serviceName);
    }

    @Override
    public RpcAddress[] getAllAddress() {
        HashSet<RpcAddress> set = new HashSet<>();
        for (RpcAddress[] addresses : addressMap.values()) {
            set.addAll(List.of(addresses));
        }
        return set.toArray(new RpcAddress[0]);
    }

    @Override
    public void removeAddress(RpcAddress rpcAddress) {


        String serviceName = rpcAddress.getServiceName();
        if (serviceName==null || serviceName.equals(""))
        {
            throw new RuntimeException("no ServiceName Can Find or you must set ServiceName");
        }
        RpcAddress[] addresses = addressMap.get(serviceName);
        if (addresses==null)
        {
            throw new RuntimeException("No match Address");
        }
        if (addresses.length==1 && addresses[0].equals(rpcAddress)) {
            addressMap.remove(serviceName);
            mappingMap.remove(serviceName);
        }else {
            List<RpcAddress> list = new ArrayList<>(List.of(addresses));
            list.remove(rpcAddress);
            addressMap.put(serviceName,list.toArray(new RpcAddress[0]));
        }



        DeleteContent content = new DeleteContent(rpcAddress);
        Server.broadcastMessage(new BroadcastMessage<>(CommandType.Delete, MessageType.broadcast, content));
    }

    @Override
    public String[] getMappings(String serviceName) {
        return mappingMap.get(serviceName);
    }

    @Override
    public String[] getAllServiceName() {
        return mappingMap.keySet().toArray(new String[0]);
    }


}
