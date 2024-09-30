package org.example.rpc.annotation;

import org.example.rpc.consumer.ConsumerPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 开启调用方自动装配
 * @Author: lyc
 * @Date: 2024/9/30
 */
@Import(ConsumerPostProcessor.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableConsumerRpc {
}
