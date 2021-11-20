package com.bdilab.dataflow.controller;

import static com.bdilab.dataflow.common.enums.FilterOperatorEnum.FILTER_OPERATORS;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.common.enums.GroupOperatorEnum;
import com.bdilab.dataflow.common.httpresult.ResultMap;
import com.bdilab.dataflow.utils.i18n.I18nUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Function Controller.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-16
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "SQL Function")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/operator")
public class FunctionController {

  private static final String PARAM_FILTER_PREFIX = "filter.";
  @Autowired
  MessageSource messageSource;

  /**
   * Gets the filter function constant with internationalization.
   */
  @GetMapping("/filter")
  @ApiOperation(value = "Filter Function")
  public ResponseEntity filterOperator() {
    return ResponseEntity.ok(filterI18nTransform());
  }

  /**
   * Transform the result to with internationalization.
   */
  private Map<String, Map<String, String>> filterI18nTransform() {
    Map<String, Map<String, String>> result = new HashMap<>();
    for (Entry<String, Map<String, String>> entry1 : FILTER_OPERATORS.entrySet()) {
      Map<String, String> tmp = new HashMap<>();
      String type = entry1.getKey();
      Map<String, String> value = entry1.getValue();
      for (Entry<String, String> entry2 : value.entrySet()) {
        String key = entry1.getKey().replace(" ", "_");
        String i18nParamName = PARAM_FILTER_PREFIX + type + "." + key;
        String i18nParamVal = I18nUtils
            .getMessage(i18nParamName);
        tmp.put(i18nParamVal, entry2.getValue());
      }
      result.put(type, tmp);
    }
    return result;
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
