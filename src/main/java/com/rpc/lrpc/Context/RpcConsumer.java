package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface RpcConsumer {

    void addService(RpcService service,RpcURL rpcURL);
    void addServices(Map<RpcService,RpcURL[]> service);
    String getRegisterServerHost();
    int getRegisterServerPort();

    public RpcMapping getRpcMappingByName(String mapping);
    public long getRequestTimeOut();




}
