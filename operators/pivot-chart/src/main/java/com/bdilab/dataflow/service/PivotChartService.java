package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author : [zhangpeiliang]
 * @description : [Service]
 */
@Service
public interface PivotChartService {
  List<Object> getPivotChart(PivotChartDescription description);

  ParamTypeRespObj getType(String type);
}
