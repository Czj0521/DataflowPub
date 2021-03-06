package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import com.bdilab.dataflow.service.PivotChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 透视图控制器.
 * @ author: [zhangpeiliang]
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "PivotChart Operator")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class PivotChartController {

  @Autowired
  private PivotChartService pivotChartService;

  @PostMapping("/pivot-chart")
  @ApiOperation(value = "透视图操作符")
  public List<Map<String, Object>> getPivotChart(@RequestBody PivotChartDescription description) {
    return pivotChartService.saveToClickHouse(description, null);
  }

  @GetMapping("/aggregation/type")
  @ApiOperation(value = "获取聚合函数类型")
  public ParamTypeRespObj getAggregationType(@RequestParam @ApiParam(value = "语言")String language) {
    return pivotChartService.getType(AggregationConstants.AGGREGATION, language);
  }

  @GetMapping("/binning/type")
  @ApiOperation(value = "获取数据分箱类型")
  public ParamTypeRespObj getBinningType(@RequestParam @ApiParam(value = "语言")String language) {
    return pivotChartService.getType(BinningConstants.BIN, language);
  }
}