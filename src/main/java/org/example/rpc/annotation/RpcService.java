package org.example.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 标记服务提供方的类
 * @Author: lyc
 * @Date: 2024/9/30
 */
@Target({ElementType.TYPE}) //注解可以应用于类、接口或枚举类型
@Retention(RetentionPolicy.RUNTIME)//表示该注解在运行时仍然可用，这样可以通过反射读取。
@Component // Spring 组件，可以被 Spring 扫描和管理。
public @interface RpcService {

    /**
     * 指定实现方,默认为实现接口中第一个
     */
    Class<?> serviceInterface() default void.class;

    /**
     * 版本
     * @return
     */
    String serviceVersion() default "1.0";
}
