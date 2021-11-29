package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.model.DataSourceStatistic;
import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.List;
import java.util.Map;

/**
 * TableJob Service.
 *
 * @author gluttony team
 * @version 1.0
 * @date 2021/09/01
 */
public interface TableJobService extends OperatorService<TableDescription> {

  DataSourceStatistic getProfiler(String tableName);

  /**
   * generate dataSource Sql from table.
   */
  String generateDataSourceSql(TableDescription tableDescription);

  List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Map<Integer, StringBuffer> preFilterMap);
}
