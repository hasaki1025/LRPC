package com.rpc.lrpc.Annotation;


import com.rpc.lrpc.RpcConsumerRunner;
import com.rpc.lrpc.RpcProviderRunner;
import com.rpc.lrpc.RpcRegisterRunner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@RPCMappingScanner
@Import({RpcRegisterRunner.class, RpcConsumerRunner.class, RpcProviderRunner.class})
public @interface EnableRpcService {
}
