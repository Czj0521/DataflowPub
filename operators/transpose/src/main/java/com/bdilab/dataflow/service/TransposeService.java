package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;

/**
 * The class defines method of transpose operator.
 *
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description: transpose operator service
 **/
public interface TransposeService extends OperatorService<TransposeDescription>  {
  /**
   * transpose operator.
   *
   * @param transposeDescription transpose's dto
   * @return sql
   */
  String transpose(TransposeDescription transposeDescription);
}
