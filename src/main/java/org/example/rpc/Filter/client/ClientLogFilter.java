package org.example.rpc.Filter.client;

import org.example.rpc.Filter.ClientBeforeFilter;
import org.example.rpc.Filter.FilterData;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * @Description
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class ClientLogFilter implements ClientBeforeFilter {
    private Logger logger= LoggerFactory.getLogger(ClientLogFilter.class);
    @Override
    public void doFilter(FilterData filterData){
        logger.info(filterData.toString());
    }

}
