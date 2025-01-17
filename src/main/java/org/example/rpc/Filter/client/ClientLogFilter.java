package org.example.rpc.Filter.client;

import org.example.rpc.Filter.ClientBeforeFilter;
import org.example.rpc.Filter.FilterData;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description 客户端的日志拦截器
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class ClientLogFilter implements ClientBeforeFilter {
    private Logger logger = LoggerFactory.getLogger(ClientLogFilter.class);
    @Override
    public void doFilter(FilterData filterData){
        logger.info(filterData.toString());
    }

}
