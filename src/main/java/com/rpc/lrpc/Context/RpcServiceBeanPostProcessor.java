package com.rpc.lrpc.Context;

import com.rpc.lrpc.Annotation.RPCController;
import com.rpc.lrpc.Annotation.RPCMapping;
import com.rpc.lrpc.message.RpcController;
import com.rpc.lrpc.message.RpcMapping;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;

@Data
@ConditionalOnBean(RPCServiceProvider.class)
@Component
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    RPCServiceProvider provider;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RPCController.class))
        {
            RpcController controller = new RpcController();
            HashSet<RpcMapping> mappings = new HashSet<>();
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(RPCMapping.class))
                {
                    //新的会覆盖旧的
                    mappings.add(new RpcMapping(method));
                }
            }
            controller.setBeanClass(bean.getClass());
            controller.setBeanName(beanName);
            controller.setServiceName(provider.getServiceName());
            controller.setRpcMappings(mappings.toArray(new RpcMapping[0]));
            provider.addMapping(mappings);
            provider.addController(controller);
        }
        return bean;
    }
}
