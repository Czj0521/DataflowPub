package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import com.bdilab.dataflow.service.PivotChartService;
import com.bdilab.dataflow.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : [zhangpeiliang]
 * @description : [透视图控制器]
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
  public R getPivotChart(@RequestBody PivotChartDescription description) {
    return pivotChartService.getPivotChart(description);
  }

  @GetMapping("/aggregation/type")
  @ApiOperation(value = "获取聚合函数类型")
  public ParamTypeRespObj getAggregationType() {
    return pivotChartService.getType(AggregationConstants.AGGREGATION);
  }

  @GetMapping("/binning/type")
  @ApiOperation(value = "获取数据分箱类型")
  public ParamTypeRespObj getBinningType() {
    return pivotChartService.getType(BinningConstants.BIN);
  }
}
