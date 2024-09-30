package org.example.rpc.annotation;

import org.example.rpc.provider.ProviderPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 开启服务提供方自动装配
 * @Author: lyc
 * @Date: 2024/9/30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ProviderPostProcessor.class)
public @interface EnableProviderRpc {
}
