package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcAddress;

public interface PushServicesRequest extends RequestContent{

   RpcService getRpcService();
   RpcAddress getRpcAddress();

   void setRpcService(RpcService service);
   void setRpcAddress(RpcAddress url);
}
