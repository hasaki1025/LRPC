package com.rpc.lrpc.Annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface RPCMappingScanner {

    /*
        需要扫描的包
     */
    String[] value();
}
