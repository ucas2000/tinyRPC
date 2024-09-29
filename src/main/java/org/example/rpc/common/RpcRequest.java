package org.example.rpc.common;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description 封装客户端发出的远程调用请求
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcRequest implements Serializable {
    private String serviceVersion;
    //目标服务的类名
    private String className;
    //要调用的方法名称
    private String methodName;
    private Object data;
    private Class dataClass;
    private Class<?>[] parameterTypes;
    private Map<String,Object> serviceAttachments;
    private Map<String,Object> clientAttachments;

    public Class getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Map<String, Object> getServiceAttachments() {
        return serviceAttachments;
    }

    public void setServiceAttachments(Map<String, Object> serviceAttachments) {
        this.serviceAttachments = serviceAttachments;
    }

    public Map<String, Object> getClientAttachments() {
        return clientAttachments;
    }

    public void setClientAttachments(Map<String, Object> clientAttachments) {
        this.clientAttachments = clientAttachments;
    }
}
