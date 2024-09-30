package org.example.rpc.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 拦截器链
 * @Author: lyc
 * @Date: 2024/9/30
 */
public class FilterChain {
    private List<Filter> filters=new ArrayList<Filter>();
    //public FilterChain() {}
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void addFilter(List<Object> filters) {
        for (Object filter : filters) {
            addFilter((Filter) filter);
        }
    }
    public void doFilter(FilterData filterData){
        for(Filter filter:filters){
            filter.doFilter(filterData);
        }
    }
}
