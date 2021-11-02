package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.common.enums.GroupOperatorEnum;
import com.bdilab.dataflow.common.httpresult.ResultMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Function Controller.

 * @author: Zunjing Chen
 * @create: 2021-09-16
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "SQL Function")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/operator")
public class FunctionController {
  /**
   * Gets the filter function constant.
   */
  @GetMapping("/filter")
  @ApiOperation(value = "Filter Function")
  public ResponseEntity filterOperator() {
    return ResponseEntity.ok(com.bdilab.dataflow.common.enums.FilterOperatorEnum.FILTER_OPERATORS);
  }

  /**
   * Gets the filter function constant.
   */
  @GetMapping("/group")
  @ApiOperation(value = "Aggregate Function")
  public ResponseEntity groupOperator() {
    Map<String, Map<String, String>> map = new HashMap<>();
    HashMap<String, String> numericMap = new HashMap<>();
    HashMap<String, String> othersMap = new HashMap<>();
    for (GroupOperatorEnum value : GroupOperatorEnum.values()) {
      numericMap.put(value.getOperatorName(), value.getSqlParam());
      if (!value.isOnlyNumeric()) {
        othersMap.put(value.getOperatorName(), value.getSqlParam());
      }
    }
    map.put("numeric", numericMap);
    map.put("others", othersMap);
    ResultMap resultMap = new ResultMap().success().payload(map);
    return ResponseEntity.ok(resultMap);
  }

}
