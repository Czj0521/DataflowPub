package com.bdilab.dataflow.service;

import com.bdilab.dataflow.model.DataSourceStatistic;

/**
 * @author gluttony team
 * @version 1.0
 * @date 2021/09/01
 * Table仪表盘-数据处理Service接口类
 */
public interface TableJobService {

    DataSourceStatistic getProfiler(String tablename);
}
