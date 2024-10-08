package org.example.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 后缀  标记类中的字段 从配置文件加载的字段
 * @Author: lyc
 * @Date: 2024/9/30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesField {
    /**
     * 默认为属性名 例如: registryType = registry-type  遵守配置文件规则
     * @return
     */
    String value() default "";
}
