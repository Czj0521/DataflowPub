package com.bdilab.dataflow.service;

import com.bdilab.dataflow.utils.dag.DagNode;

/**
 * statistical test operator service.

 * @author YuShaochao
 * @create 2021-12-20
 */
public interface StatisticalTestService {
  public double getPValue(DagNode dagNode);
}
