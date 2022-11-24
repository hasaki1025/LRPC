package com.rpc.lrpc.Util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionServer implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("RPC.Provider.port");
        String property1 = context.getEnvironment().getProperty("RPC.Register.port");
        return (property!=null && !"".equals(property))|| (property1!=null && !"".equals(property1));
    }
}
