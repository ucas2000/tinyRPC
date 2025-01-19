package org.example.rpc.pool;

import io.netty.channel.ChannelHandlerContext;
import org.example.rpc.common.RpcRequest;
import org.example.rpc.common.RpcResponse;
import org.example.rpc.common.RpcServiceNameBuilder;
import org.example.rpc.common.constant.MsgStatus;
import org.example.rpc.common.constant.MsgType;
import org.example.rpc.protocol.MsgHeader;
import org.example.rpc.protocol.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class ThreadPoolFactory {
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private static ThreadPoolExecutor slowPoll;

    private static ThreadPoolExecutor fastPoll;

    private static volatile ConcurrentHashMap<String, AtomicInteger> slowTaskMap = new ConcurrentHashMap();

    private static int corSize = Runtime.getRuntime().availableProcessors();
    // 缓存服务 该缓存放这里不太好,应该作一个统一 Config 进行管理
    private static Map<String, Object> rpcServiceMap;

    static {
        slowPoll = new ThreadPoolExecutor(corSize / 2, corSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2000),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("slow poll-" + r.hashCode());
                    thread.setDaemon(true);
                    return thread;
                });

        fastPoll = new ThreadPoolExecutor(corSize, corSize * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("fast poll-" + r.hashCode());
                    thread.setDaemon(true);
                    return thread;
                });
        startClearMonitor();
        }
    private ThreadPoolFactory() {}

    public static void setRpcServiceMap(Map<String, Object> rpcMap){
        rpcServiceMap = rpcMap;
    }
    /**
     * 清理慢请求
     */
    private static void startClearMonitor(){
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(()->{
            slowTaskMap.clear();
        },5,5,TimeUnit.MINUTES);
    }

    public static void submitRequest(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol){

        // 获取 RPC 请求对象
        final RpcRequest request = protocol.getBody();

        // 生成请求的唯一标识 key
        String key = request.getClassName() + request.getMethodName() + request.getServiceVersion();

        // 默认使用 fastPoll 线程池
        ThreadPoolExecutor poll = fastPoll;

        // 如果该请求在 slowTaskMap 中记录的超时次数 >= 10，则使用 slowPoll 线程池
        if (slowTaskMap.containsKey(key) && slowTaskMap.get(key).intValue() >= 10) {
            poll = slowPoll;
        }

        // 提交任务到线程池
        poll.submit(() -> {
            // 创建 RPC 响应协议对象
            RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();

            // 获取请求头
            final MsgHeader header = protocol.getHeader();

            // 创建 RPC 响应对象
            RpcResponse response = new RpcResponse();

            // 记录任务开始时间
            long startTime = System.currentTimeMillis();

            try {
                // 执行业务逻辑
                final Object result = submit(ctx, protocol);

                // 设置响应数据
                response.setData(result);
                response.setDataClass(result == null ? null : result.getClass());

                // 设置响应状态为成功
                header.setStatus((byte) MsgStatus.SUCCESS.ordinal());
            } catch (Exception e) {
                // 执行业务失败，设置响应状态为失败，并记录异常
                header.setStatus((byte) MsgStatus.FAILED.ordinal());
                response.setException(e);
                logger.error("process request {} error", header.getRequestId(), e);
            } finally {
                // 计算任务执行时间
                long cost = System.currentTimeMillis() - startTime;
                System.out.println("cost time:" + cost);

                // 如果任务执行时间超过 1000ms，则记录超时次数
                if (cost > 1000) {
                    final AtomicInteger timeOutCount = slowTaskMap.putIfAbsent(key, new AtomicInteger(1));
                    if (timeOutCount != null) {
                        timeOutCount.incrementAndGet();
                    }
                }
            }

            // 设置响应协议的头和体
            resProtocol.setHeader(header);
            resProtocol.setBody(response);

            // 记录日志
            logger.info("执行成功: {},{},{},{}", Thread.currentThread().getName(), request.getClassName(), request.getMethodName(), request.getServiceVersion());

            // 将响应写回客户端
            ctx.fireChannelRead(resProtocol);
        });
    }

    /**
     * @description: 处理 RPC 请求并返回业务逻辑的执行结果
     * @author: lyc
     * @date: 2025/1/19 17:45
     * @param ctx
    * @param protocol
     * @return:
     **/
    private static Object submit(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception{
        // 创建 RPC 响应协议对象
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();

        // 获取请求头
        MsgHeader header = protocol.getHeader();

        // 设置响应头的消息类型为 RESPONSE
        header.setMsgType((byte) MsgType.RESPONSE.ordinal());

        // 获取请求体
        final RpcRequest request = protocol.getBody();

        // 执行业务逻辑并返回结果
        return handle(request);
    }

    // 调用方法
    private static Object handle(RpcRequest request) throws Exception {
        // 构建服务键
        String serviceKey = RpcServiceNameBuilder.buildServiceKey(request.getClassName(), request.getServiceVersion());

        // 从服务映射中获取服务实例
        Object serviceBean = rpcServiceMap.get(serviceKey);

        // 如果服务实例不存在，抛出异常
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        // 获取服务类的 Class 对象
        Class<?> serviceClass = serviceBean.getClass();

        // 获取方法名
        String methodName = request.getMethodName();

        // 获取方法参数类型
        Class<?>[] parameterTypes = request.getParameterTypes();

        // 获取方法参数值
        Object[] parameters = {request.getData()};

        // 使用 FastClass 创建服务类的快速调用对象
        FastClass fastClass = FastClass.create(serviceClass);

        // 获取方法的索引
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);

        // 调用方法并返回结果
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }

}
