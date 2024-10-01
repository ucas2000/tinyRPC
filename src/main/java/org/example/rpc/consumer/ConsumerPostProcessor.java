package org.example.rpc.consumer;

import org.example.rpc.Filter.FilterConfig;
import org.example.rpc.Filter.client.ClientLogFilter;
import org.example.rpc.annotation.RpcReference;
import org.example.rpc.config.RpcProperties;
import org.example.rpc.protocol.serialization.SerializationFactory;
import org.example.rpc.utils.PropertiesUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;

import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Description
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class ConsumerPostProcessor implements BeanPostProcessor , EnvironmentAware , InitializingBean {
    private Logger logger= LoggerFactory.getLogger(ClientLogFilter.class);
    RpcProperties rpcProperties;

    /**
     * 从配置文件中读取配置
     */
    @Override
    public void setEnvironment(Environment environment) {
        RpcProperties properties=RpcProperties.getInstance();
        PropertiesUtils.init(properties,environment);
        rpcProperties=properties;
        logger.info("读取配置文件成功");
    }
    /**
     * 初始化一些bean
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        SerializationFactory.init();
        RegistryFactory.init();
        LoadBalancerFactory.init();
        FilterConfig.initClientFilter();
    }

    /**
     * 代理层注入
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //获取所有字段
        final Field[] fields=bean.getClass().getDeclaredFields();
        //遍历所有字段找到 RpcReference 注解的字段
        for(Field field:fields){
            if(field.isAnnotationPresent(RpcReference.class)){
                final RpcReference rpcReference=field.getAnnotation(RpcReference.class);
                final Class<?> clazz=field.getType();
                field.setAccessible(true);
                Object obj=null;
                try{
                    //创建代理对象
                    obj= Proxy.newProxyInstance(clazz.getClassLoader(),
                            new Class<?>[]{clazz},
                            new RpcInvokerProxy(rpcReference.serviceVersion(),rpcReference.timeout(),rpcReference.faultTolerant(),
                            rpcReference.loadBalancer(),rpcReference.retryCount()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    field.set(bean,obj);
                    field.setAccessible(false);
                    logger.info(beanName + " field:" + field.getName() + "注入成功");
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                    logger.info(beanName + " field:" + field.getName() + "注入失败");
                }
            }
        }
        return bean;
    }
}
