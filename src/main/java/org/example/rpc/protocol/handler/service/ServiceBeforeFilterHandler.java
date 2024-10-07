package org.example.rpc.protocol.handler.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.rpc.Filter.FilterConfig;
import org.example.rpc.Filter.FilterData;
import org.example.rpc.common.RpcRequest;
import org.example.rpc.common.RpcResponse;
import org.example.rpc.common.constant.MsgStatus;
import org.example.rpc.protocol.MsgHeader;
import org.example.rpc.protocol.RpcProtocol;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

/**
 * @Description 前置拦截器
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class ServiceBeforeFilterHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private Logger logger = LoggerFactory.getLogger(ServiceBeforeFilterHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) {
        final RpcRequest request=msg.getBody();
        final FilterData filterData = new FilterData(request);
        RpcResponse response = new RpcResponse();
        MsgHeader header = msg.getHeader();
        try{
            FilterConfig.getServiceBeforeFilterChain().doFilter(filterData);
        }catch (Exception e){
            RpcProtocol<RpcResponse> rpcProtocol=new RpcProtocol<>();
            header.setStatus((byte) MsgStatus.FAILED.ordinal());
            response.setException(e);
            logger.error("before process request {} error", header.getRequestId(), e);
            rpcProtocol.setHeader(header);
            rpcProtocol.setBody(response);
            ctx.writeAndFlush(rpcProtocol);
            return;
        }
        ctx.writeAndFlush(msg);
    }

}
