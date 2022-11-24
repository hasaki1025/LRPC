package com.rpc.lrpc.Context;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

import java.util.Map;

public interface RpcConsumer {

    void addService(RpcService service, RpcAddress rpcAddress);
    void addServices(Map<RpcService, RpcAddress[]> service);
    String getRegisterServerHost();
    int getRegisterServerPort();

    public RpcMapping getRpcMappingByName(String mapping);
    public long getRequestTimeOut();

    public boolean containAddress(RpcAddress rpcAddress);


}
