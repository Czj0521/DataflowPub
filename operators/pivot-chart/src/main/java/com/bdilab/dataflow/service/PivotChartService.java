package com.bdilab.dataflow.service;

import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;

import com.bdilab.dataflow.utils.R;
import org.springframework.stereotype.Service;

/**
 * @author : [zhangpeiliang]
 * @description : [Service]
 */
@Service
public interface PivotChartService {
  R getPivotChart(PivotChartDescription description);

  ParamTypeRespObj getType(String type,String language);
}
