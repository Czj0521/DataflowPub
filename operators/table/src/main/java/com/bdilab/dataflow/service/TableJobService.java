package com.bdilab.dataflow.service;

import com.bdilab.dataflow.model.DataSourceStatistic;

/**
 * TableJob Service.

 * @author gluttony team
 * @version 1.0
 * @date 2021/09/01
 */
public interface TableJobService {
  DataSourceStatistic getProfiler(String tablename);
}
