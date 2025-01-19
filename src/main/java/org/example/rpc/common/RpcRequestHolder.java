package org.example.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description 管理和跟踪 RPC 请求
 * 当客户端发送一个 RPC 请求时，会将请求 ID 和 RpcFuture 对象存入 REQUEST_MAP
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcRequestHolder {
    // 请求id
    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    // 一个全局的 REQUEST_MAP 来存储每个 RPC 请求的 RpcFuture 对象
    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
