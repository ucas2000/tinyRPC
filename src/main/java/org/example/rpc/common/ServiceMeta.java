package org.example.rpc.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Description 封装服务的元信息,主要用于服务注册、发现和故障转移
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class ServiceMeta implements Serializable {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;

    /**
     * 关于redis注册中心的属性
     */
    private long endTime;

    private String UUID;
    /**
     * 故障转移需要移除不可用服务
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj==null|| getClass()!=obj.getClass()) return false;
        ServiceMeta other = (ServiceMeta) obj;
        return  servicePort==other.servicePort&&
                Objects.equals(serviceName,other.serviceName)&&
                Objects.equals(serviceVersion, other.serviceVersion) &&
                Objects.equals(serviceAddr, other.serviceAddr) &&
                Objects.equals(UUID, other.UUID);
    }
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceAddr() {
        return serviceAddr;
    }

    public void setServiceAddr(String serviceAddr) {
        this.serviceAddr = serviceAddr;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceVersion, serviceAddr, servicePort, UUID);
    }

}
