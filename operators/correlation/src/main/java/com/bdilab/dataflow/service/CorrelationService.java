package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobDescription.CorrelationDescription;
import com.bdilab.dataflow.dto.joboutputjson.ResponseObj;
import java.util.List;

/**
 * CorrelationService.
 *
 * @author Liu Pan
 * @date 2021-12-23
 **/
public interface CorrelationService {
  public List<ResponseObj> correlation(CorrelationDescription correlationDescription);
}
