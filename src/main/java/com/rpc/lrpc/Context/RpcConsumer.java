package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcConsumer {

    Object comsumer(String serviceName,String mapping,Object[] params);
    void addService(RpcService service);
    void addServices(Map<RpcService,RpcURL[]> service);
    void updateServices(RpcService rpcService, RpcURL rpcURL);

    String getRegisterServerHost();
    int getRegisterServerPort();

    public RpcMapping getRpcMappingByName(String mapping);
    public long getRequestTimeOut();







}
