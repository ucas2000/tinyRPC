package org.example.rpc.common;

/**
 * @Description 构建服务的唯一标识符
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcServiceNameBuilder {
    // key: 服务名 value: 服务提供方s
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("$", serviceName, serviceVersion);
    }

}
