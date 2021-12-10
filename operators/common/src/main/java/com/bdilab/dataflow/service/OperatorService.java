package com.bdilab.dataflow.service;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.List;
import java.util.Map;

/**
 * All operator which will return data need to implements this interface. Operator like
 * filter,join,transpose,transform do not need to implements.
 *
 * @author Zunjing Chen
 * @date 2021-11-03
 **/
public interface OperatorService<T extends JobDescription> {

  /**
   * Execute the job and return data.
   *
   * @param jobDescription job desc
   * @return data
   */
  @Deprecated
  List<Map<String, Object>> execute(T jobDescription);

  /**
   * linkage: 1. Read the data source and add filter according to preFilterMap.
   *          2. save the result to ClickHouse.
   *          3. return the result.
   *
   * @param dagNode DagNode Object, in order to get node description.
   * @param preFilterMap the filters' string of different datasource.
   * @return data
   */
  List<Map<String, Object>> saveToClickHouse(DagNode dagNode,
                                             Map<Integer, StringBuffer> preFilterMap);
}
