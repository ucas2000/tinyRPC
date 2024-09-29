package org.example.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description 处理异步调用,它封装了一个Promise对象，用于表示RPC请求的结果，允许用户在请求完成时获取结果或异常。
 * @Author: lyc
 * @Date: 2024/9/26
 */
public class RpcFuture<T> {
    private Promise<T> promise;
    private long timeout;
    public Promise<T> getPromise() {
        return promise;
    }
    public void setPromise(Promise<T> promise) {
        this.promise = promise;
    }
    public long getTimeout() {
        return timeout;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public RpcFuture() {
    }

    public RpcFuture(Promise<T> promise,long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
