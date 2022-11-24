package com.rpc.lrpc.Annotation;


import com.rpc.lrpc.RpcConsumerRunner;
import com.rpc.lrpc.RpcProviderRunner;
import com.rpc.lrpc.RpcRegisterRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RpcRegisterRunner.class, RpcConsumerRunner.class, RpcProviderRunner.class})
@PropertySource({"application-Rpc.properties"})
public @interface EnableRpcService {

}
