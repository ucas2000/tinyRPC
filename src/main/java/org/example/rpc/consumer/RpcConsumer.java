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
 * @Description 消费方发送数据
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class RpcConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private Logger logger = LoggerFactory.getLogger(RpcConsumer.class);

    public RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
            }
    /**
     * 发送请求
     */
    public void sentRequest(RpcProtocol<RpcRequest> protocol, ServiceMeta serviceMetadata) throws Exception{
        if(serviceMetadata!=null){
            ChannelFuture future=bootstrap.connect(serviceMetadata.getServiceAddr(),serviceMetadata.getServicePort()).sync();
            future.addListener((ChannelFutureListener) arg0->{
                if(future.isSuccess()){
                    logger.info("连接 rpc server {} 端口 {} 成功.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                }else{
                    logger.error("连接 rpc server {} 端口 {} 失败.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            // 写入数据
            future.channel().writeAndFlush(protocol);
        }
    }
}
