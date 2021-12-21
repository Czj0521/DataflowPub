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
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
  public ParamTypeRespObj getAggregationType(@RequestParam @ApiParam(value = "语言")String language) {
    return pivotChartService.getType(AggregationConstants.AGGREGATION,language);
  }

  @GetMapping("/binning/type")
  @ApiOperation(value = "获取数据分箱类型")
  public ParamTypeRespObj getBinningType(@RequestParam @ApiParam(value = "语言")String language) {
    return pivotChartService.getType(BinningConstants.BIN,language);
  }

  @PostMapping("/binning/test")
  @ApiOperation(value = "测试")
  public List<Map<String, Object>> test(@RequestBody PivotChartDescription description) {
    return pivotChartService.saveToClickHouse(description, null);
  }
}
