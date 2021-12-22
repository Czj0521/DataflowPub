package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Materialize Job Service.
 *
 * @author: wh
 * @create: 2021-10-27
 */
public interface MaterializeJobService {
  /**
   * Materialize Job.
   *
   * @return MaterializeId
   */
  JSONObject materialize(String operatorId);

  /**
   * Delete materialize Job.
   */
  JSONObject deleteMaterialize(String operatorId);
}
