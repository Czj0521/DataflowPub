package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.dto.joboutputjson.MaterializeOutputJson;

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
   * @return MaterializeOutputJson
   */
  MaterializeOutputJson materialize(MaterializeInputJson materializeInputJson);

  /**
   * Materialize Job for linking.
   *
   * @param subTableSql is not null
   * @return materializeOutputJson
   */
  JSONObject materialize(String subTableSql);

  /**
   * Delete Materializem View.
   */
  String deleteSubTable(String subTableId);


}
