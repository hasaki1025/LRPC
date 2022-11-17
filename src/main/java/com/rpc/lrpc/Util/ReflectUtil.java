package com.rpc.lrpc.Util;


import com.rpc.lrpc.Annotation.RPCMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {



    public static Method[] getRPCMethod(Class<?> clazz)
    {
        ArrayList<Method> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RPCMapping.class))
                list.add(method);
        }
        return list.toArray(new Method[0]);
    }
}
