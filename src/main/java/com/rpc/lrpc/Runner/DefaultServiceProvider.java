package com.rpc.lrpc.Runner;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.Util.ReflectUtil;
import com.rpc.lrpc.message.RpcMapping;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Data
@Component
public class DefaultServiceProvider implements RPCServiceProvider, BeanPostProcessor {
    @Value("${RPC.Provider.ServiceName}")
    String serviceName;
    @Value("${RPC.Provider.port}")
    int port;
    RpcMapping[] mappings;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(RPCController.class))
        {

            for (Method method : ReflectUtil.getRPCMethod(beanClass)) {

            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
