package com.rpc.lrpc.message.Content.Request;

import com.rpc.lrpc.message.RpcService;
import com.rpc.lrpc.message.RpcURL;

import java.util.Map;

public interface PushServicesRequest extends RequestContent{

   RpcService getRpcService();
   RpcURL getRpcURL();

   void setRpcService(RpcService service);
   void setRpcURL(RpcURL url);
}
