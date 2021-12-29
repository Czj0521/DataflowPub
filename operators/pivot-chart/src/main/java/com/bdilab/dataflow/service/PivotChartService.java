package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import com.bdilab.dataflow.utils.R;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Service.
 * @ author: [zhangpeiliang]
 */
@Service
public interface PivotChartService {

  @SuppressWarnings("unused")
  R getPivotChart(PivotChartDescription description);

  List<Map<String, Object>> saveToClickHouse(PivotChartDescription description,
                                             List<String> brushFilters);

  ParamTypeRespObj getType(String type, String language);
}