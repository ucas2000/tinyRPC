package org.example.rpc.registry;

import org.example.rpc.spi.ExtensionLoader;

/**
 * @Description 注册工厂
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class RegistryFactory {
    public static RegistryService get(String registryService) throws Exception{
        return ExtensionLoader.getInstance().get(registryService);
    }
    public static void init() throws Exception {
        ExtensionLoader.getInstance().loadExtension(RedisRegistry.class);
    }

}
