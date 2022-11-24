package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Data
public class DefaultPushServicesRequest implements PushServicesRequest {

    RpcService rpcService;
    RpcURL rpcURL;


   @Override
   public void setRpcService(RpcService service) {
      this.rpcService=service;
   }

   @Override
   public void setRpcURL(RpcURL url) {
      this.rpcURL=url;
   }
}
