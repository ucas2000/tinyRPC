package org.example.rpc.registry;

import org.example.rpc.common.ServiceMeta;

import java.io.IOException;
import java.util.List;

/**
 * @Description 注册中心接口
 * @Author: lyc
 * @Date: 2024/9/30
 */
public interface RegistryService {

    /**
     * 服务注册
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务注销
     * @param serviceMeta
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 获取 serviceName 下的所有服务
     * @param serviceName
     * @return
     */
    List<ServiceMeta> discoveries(String serviceName);
    /**
     * 关闭
     * @throws IOException
     */
    void destroy() throws IOException;
}
