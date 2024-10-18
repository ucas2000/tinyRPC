package org.example.rpc.router;

import org.example.rpc.spi.ExtensionLoader;

/**
 * @Description
 * @Author: lyc
 * @Date: 2024/10/14
 */
public class LoadBalancerFactory {
    public static LoadBalancer get(String serviceLoadBalancer) throws Exception {

        return ExtensionLoader.getInstance().get(serviceLoadBalancer);

    }

    public static void init() throws Exception {
        ExtensionLoader.getInstance().loadExtension(LoadBalancer.class);
    }
}
