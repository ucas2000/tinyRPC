package org.example.rpc.utils;

import org.example.rpc.annotation.PropertiesField;
import org.example.rpc.annotation.PropertiesPrefix;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

/**
 * @Description 根据注解从配置环境中加载属性并设置到对象的字段中
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class PropertiesUtils {
    /**
     * 根据对象中的配置匹配配置文件
     * @param o
     * @param environment
     */
    public static void inti(Object o, Environment environment) {
        final Class<?> clazz = o.getClass(); //对象的类信息
        //获取 @PropertiesPrefix 注解，检查其是否存在，并提取前缀。如果没有. 则在后面加上一个 .
        final PropertiesPrefix propertiesPrefix=clazz.getAnnotation(PropertiesPrefix.class);
        if(propertiesPrefix == null) {
            throw new NullPointerException(clazz+ " @PropertiesPrefix 不存在");
        }
        String prefix=propertiesPrefix.value();
        // 前缀参数矫正
        if(!prefix.contains(".")){
            prefix+=".";
        }
        //使用 getDeclaredFields() 方法遍历类中的所有字段
        for(Field field : clazz.getDeclaredFields()){
            //检查每个字段是否有 @PropertiesField 注解
            final PropertiesField fieldAnnotation=field.getAnnotation(PropertiesField.class);
            if(fieldAnnotation == null) {
                continue;
            }
            String fieldValue=fieldAnnotation.value();
            //如果 @PropertiesField 注解中定义的值为空，使用 convertToHyphenCase 方法将字段名转换为连字符格式。
            if(fieldValue==null|| fieldValue.equals("")){
                fieldValue = convertToHyphenCase(field.getName());
            }
            try{
                field.setAccessible(true);
                final Class<?> fieldType=field.getType();
                //获取对应的属性值
                final Object value=PropertyUtil.handle(environment, prefix + fieldValue, fieldType);
                if(value == null){
                    continue;
                }
                field.set(o, value);

            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
    }

    private static String convertToHyphenCase(String name) {
        StringBuffer output=new StringBuffer();
        for(int i=0;i<name.length();i++){
            char c=name.charAt(i);
            if(Character.isUpperCase(c)){
                output.append("-");
                output.append(Character.toLowerCase(c));
            }else{
                output.append(c);
            }
        }
        return output.toString();
    }
}
