package org.example.rpc.protocol.handler.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.rpc.common.RpcRequest;
import org.example.rpc.poll.ThreadPollFactory;
import org.example.rpc.protocol.RpcProtocol;

/**
 * @Description 处理接收到的RPC请求的处理器
 * @Author: lyc
 * @Date: 2024/9/29
 */

//ctx是当前通道的上下文，用于发送响应或管理通道。msg是接收到的RPC请求消息
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    public RpcRequestHandler() {}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg){
        ThreadPollFactory.submitRequest(ctx,msg);
    }
}
