package org.example.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description 管理和跟踪 RPC 请求
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcRequestHolder {
    // 请求id
    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    // 绑定请求
    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
