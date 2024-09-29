package org.example.rpc.protocol.handler;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description RPC请求的异步执行
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcRequestProcessor {
    private static volatile ThreadPoolExecutor threadPoolExecutor;
    //双重检查锁定,确保线程安全地初始化
    public static void submitRequest(Runnable task) {
        if(threadPoolExecutor==null){
            synchronized (RpcRequestProcessor.class){
                if(threadPoolExecutor==null){
                    threadPoolExecutor= new ThreadPoolExecutor(10,15,60L,
                            TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10000));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
