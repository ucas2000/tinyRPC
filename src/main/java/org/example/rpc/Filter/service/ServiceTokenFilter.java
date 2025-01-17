package org.example.rpc.Filter.service;

import org.example.rpc.Filter.FilterData;
import org.example.rpc.Filter.ServiceBeforeFilter;
import org.example.rpc.config.RpcProperties;
import java.util.Map;


/**
 * @Description service 的 token 拦截器
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class ServiceTokenFilter implements ServiceBeforeFilter {
    @Override
    public void doFilter(FilterData data) {
        final Map<String, Object> attachments=data.getClientAttachments();
        final Map<String, Object> serviceAttachments=data.getServiceAttachments();
        if(!attachments.getOrDefault("token","").equals(serviceAttachments.getOrDefault("token",""))){
            throw new IllegalArgumentException("token错误");
        }
    }
}
