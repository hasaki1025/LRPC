package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;
import lombok.Data;

@Data
public class DefaultPushServicesRequest implements PushServicesRequest {

    RpcService rpcService;
    RpcAddress rpcAddress;


   @Override
   public void setRpcService(RpcService service) {
      this.rpcService=service;
   }

   public void setRpcAddress(RpcAddress url) {
      this.rpcAddress =url;
   }
}
