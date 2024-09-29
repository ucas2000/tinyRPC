package org.example.rpc.protocol.serialization;

import java.io.IOException;


/**
 * @Description  序列化接口
 * @Author: lyc
 * @Date: 2024/9/29
 */
public interface RpcSerialization {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
