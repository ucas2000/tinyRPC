package org.example.rpc.protocol.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.rpc.common.RpcRequest;
import org.example.rpc.common.RpcResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;


import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * @Description JSON
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class JsonSerialization implements RpcSerialization {


    private static final ObjectMapper MAPPER;

    static {
        MAPPER = generateMapper(JsonInclude.Include.ALWAYS);
    }

    /**
     * 创建自定义的ObjectMapper实例，设置序列化包含规则和反序列化行为，如忽略未知属性和处理日期格式
     * @param include
     * @return
     */
    private static ObjectMapper generateMapper(JsonInclude.Include include) {
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.setSerializationInclusion(include);
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return customMapper;
    }

    /**
     * 序列化方法
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return obj instanceof String ? ((String) obj).getBytes() : MAPPER.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 解决jackson在反序列化对象时为LinkedHashMap
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        final T t = MAPPER.readValue(data, clz);
        if (clz.equals(RpcRequest.class)) {
            RpcRequest rpcRequest = ((RpcRequest)t);

            rpcRequest.setData(convertReq(rpcRequest.getData(),rpcRequest.getDataClass()));
            return (T) rpcRequest;
        }else{
            RpcResponse rpcResponse = ((RpcResponse)t);
            rpcResponse.setData(convertRes(rpcResponse.getData(),rpcResponse.getDataClass()));
            return (T) rpcResponse;
        }
    }

    public Object convertReq(Object data,Class clazz) {
        if (ObjectUtils.isEmpty(data)){
            return null;
        }
        final Object o = ((ArrayList) data).get(0);
        if (BeanUtils.isSimpleProperty(o.getClass())){
            return o;
        }
        final LinkedHashMap map = (LinkedHashMap) o;
        return convert(clazz,map);
    }
    public Object convertRes(Object data,Class clazz)  {
        if (ObjectUtils.isEmpty(data)){
            return null;
        }
        final Object o = ((ArrayList) data).get(0);
        if (BeanUtils.isSimpleProperty(o.getClass())){
            return o;
        }
        final LinkedHashMap map = (LinkedHashMap) data;
        return convert(clazz,map);
    }

    /**
     * LinkedHashMap 属性名和属性值的映射
     * @param clazz
     * @param map
     * @return
     */
    public Object convert(Class clazz,LinkedHashMap map) {
        // 额外处理对象
        final Class dataClass = clazz;

        try {
            Object o = dataClass.newInstance();
            map.forEach((k,v)->{
            //k: 属性名。v: 属性值。

                try {
                    final Field field = dataClass.getDeclaredField(String.valueOf(k));
                    if (v!=null && v.getClass().equals(LinkedHashMap.class)){
                        v = convert(field.getType(),(LinkedHashMap) v);
                    }
                    field.setAccessible(true);
                    field.set(o,v);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}

