package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.common.enums.FilterOperatorEnum;
import com.bdilab.dataflow.common.enums.GroupOperatorEnum;
import com.bdilab.dataflow.common.httpresult.ResultMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-16
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "操作符")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/operator")
public class OperatorController {
    /**
     * 获取过滤操作符常量，以及每个操作对应的sql替换符
     * 操作符分四类：数字，字符串，时间，布尔
     */
    @GetMapping("/filter")
    @ApiOperation(value = "过滤操作符")
    public ResponseEntity filterOperator() {
        Map<String, Map<String, String>> objectObjectHashMap = new HashMap<>();
        for (FilterOperatorEnum value : FilterOperatorEnum.values()) {
            Map<String, String> orDefault = objectObjectHashMap.getOrDefault(value.getDataType(), new HashMap<>());
            orDefault.put(value.getFilterOperatorName(), value.getSQLParam());
            objectObjectHashMap.put(value.getDataType(), orDefault);
        }
        ResultMap resultMap = new ResultMap().success().payload(objectObjectHashMap);
        return ResponseEntity.ok(resultMap);
    }

    /**
     * 获取聚合操作符常量，以及每个操作对应的sql替换符
     * 操作符分两类：
     * 1. 数值类型字段 ： 操作符全部包含
     * 2. 非数字字段：只包含部分
     */
    @GetMapping("/group")
    @ApiOperation(value = "聚合操作符")
    public ResponseEntity groupOperator() {
        Map<String, Map<String, String>> map = new HashMap<>();
        HashMap<String, String> numericMap = new HashMap<>();
        HashMap<String, String> othersMap = new HashMap<>();
        for (GroupOperatorEnum value : GroupOperatorEnum.values()) {
            numericMap.put(value.getOperatorName(), value.getSQLParam());
            if(!value.isOnlyNumeric()){
                othersMap.put(value.getOperatorName(), value.getSQLParam());
            }
        }
        map.put("numeric",numericMap);
        map.put("others",othersMap);
        ResultMap resultMap = new ResultMap().success().payload(map);
        return ResponseEntity.ok(resultMap);
    }

}
