package com.rpc.lrpc.Annotation;

import com.rpc.lrpc.Runner.RPCRunner;
import org.springframework.context.annotation.Import;

@RPCMappingScanner
@Import(RPCRunner.class)
public @interface EnableRPC {
    /*
        服务名称
     */
    String value() default "";
}
