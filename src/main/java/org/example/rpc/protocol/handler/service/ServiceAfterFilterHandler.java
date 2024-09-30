package org.example.rpc.protocol.handler.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.rpc.Filter.FilterConfig;
import org.example.rpc.Filter.FilterData;
import org.example.rpc.Filter.client.ClientLogFilter;
import org.example.rpc.common.RpcResponse;
import org.example.rpc.common.constant.MsgStatus;
import org.example.rpc.protocol.MsgHeader;
import org.example.rpc.protocol.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @Description 处理RPC响应的过滤器
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class ServiceAfterFilterHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private Logger logger = LoggerFactory.getLogger(ClientLogFilter.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg)  {
        final FilterData filterData=new FilterData();
        filterData.setData(msg.getBody());
        RpcResponse response=new RpcResponse();
        MsgHeader header=msg.getHeader();
        try{
            FilterConfig.getServiceAfterFilterChain().doFilter(filterData);
        }catch (Exception e){
            header.setStatus((byte) MsgStatus.FAILED.ordinal());
            response.setException(e);
            logger.error("after process request {} error", header.getRequestId(), e);
        }
        ctx.writeAndFlush(msg);
    }
}
