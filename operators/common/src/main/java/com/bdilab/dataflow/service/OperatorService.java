package com.bdilab.dataflow.service;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
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
  List<Map<String, Object>> execute(T jobDescription);
}
