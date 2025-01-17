package org.example.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.rpc.common.RpcRequest;
import org.example.rpc.common.ServiceMeta;
import org.example.rpc.protocol.RpcProtocol;
import org.example.rpc.protocol.codec.RpcDecoder;
import org.example.rpc.protocol.codec.RpcEncoder;
import org.example.rpc.protocol.handler.consumer.RpcResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 客户端：消费方发送数据
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class RpcConsumer {
    private final Bootstrap bootstrap;  //Netty 提供的 Bootstrap 类的实例，用于设置客户端的连接参数,用于配置和引导客户端
    private final EventLoopGroup eventLoopGroup; //负责处理 I/O 事件
    private Logger logger = LoggerFactory.getLogger(RpcConsumer.class);

    public RpcConsumer() {
        bootstrap = new Bootstrap();// 创建 Netty 客户端启动器
        eventLoopGroup = new NioEventLoopGroup();// 创建一个用于处理网络事件的事件循环组（使用 NIO）
        bootstrap.group(eventLoopGroup)// 设置线程组
                .channel(NioSocketChannel.class) // 将事件循环组绑定到启动器上
                .option(ChannelOption.SO_KEEPALIVE, true)// 设置 TCP 连接保持活动的选项
                .handler(new ChannelInitializer<SocketChannel>(){ // 配置处理程序，初始化通道
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()// 获取通道的管道（Pipeline）
                                .addLast(new RpcEncoder())// 添加自定义的 RPC 编码器，用于将请求消息编码为字节流
                                .addLast(new RpcDecoder()) // 添加自定义的 RPC 解码器，用于将字节流解码为请求消息
                                .addLast(new RpcResponseHandler());// 添加自定义的 RPC 响应处理器，用于处理服务器返回的响应
                    }
                });
            }
    /**
     * 发送请求
     */
    public void sentRequest(RpcProtocol<RpcRequest> protocol, ServiceMeta serviceMetadata) throws Exception{
        //确保有有效的服务地址和端口
        if(serviceMetadata!=null){
            //使用 bootstrap 连接指定的服务地址和端口。调用 sync() 方法(阻塞)使当前线程等待连接完成，并返回一个 ChannelFuture 对象，表示连接的结果
            ChannelFuture future=bootstrap.connect(serviceMetadata.getServiceAddr(),serviceMetadata.getServicePort()).sync();
            // 添加连接监听器，处理连接结果
            future.addListener((ChannelFutureListener) arg0->{
                if(future.isSuccess()){
                    // 连接成功
                    logger.info("连接 rpc server {} 端口 {} 成功.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                }else{
                    // 连接失败，打印错误日志并关闭事件循环组
                    logger.error("连接 rpc server {} 端口 {} 失败.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            // 如果连接成功，通过 future.channel() 获取通道，并使用 writeAndFlush(protocol) 方法写入请求消息然后发送至服务提供方
            future.channel().writeAndFlush(protocol);
        }
    }
}
