package com.rpc.lrpc.Runner;

import com.rpc.lrpc.message.RpcMapping;
import com.rpc.lrpc.message.RpcService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public interface RPCServiceProvider {

    String getServiceName();
    int getPort();
    RpcMapping[] getMappings();


}
