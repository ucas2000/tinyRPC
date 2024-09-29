package org.example.rpc.protocol.handler.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.rpc.common.RpcFuture;
import org.example.rpc.common.RpcRequestHolder;
import org.example.rpc.common.RpcResponse;
import org.example.rpc.protocol.RpcProtocol;

/**
 * @Description 处理RPC响应消息
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg){
        long requestId=msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future= RpcRequestHolder.REQUEST_MAP.remove(requestId);
    }
}
