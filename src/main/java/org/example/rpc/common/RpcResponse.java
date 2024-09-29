package org.example.rpc.common;

import java.io.Serializable;

/**
 * @Description 封装RPC调用的响应信息
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcResponse implements Serializable {
    private Object data;
    private Class dataClass;
    private String message;
    private Exception exception;

    public Class getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
