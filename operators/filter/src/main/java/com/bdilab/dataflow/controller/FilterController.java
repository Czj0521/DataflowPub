package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.common.httpresult.ResultMap;
import com.bdilab.dataflow.service.impl.FilterJobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Map;

import static com.bdilab.dataflow.common.enums.FilterOperatorEnum.FILTER_OPERATORS;

/**
 * @author: wh
 * @create: 2021-10-25
 * @description: Filter Operator Controller
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Filter Operator")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/operator")
public class FilterController {
    @Resource
    FilterJobServiceImpl filterJobService;

    /**
     * Get the constant of the filter operator and the SQL substitution corresponding to each operation.
     * Four types of operators: number, string, time, Boolean
     */
    @GetMapping("/getFilterOperators")
    @ApiOperation(value = "Get Filter Operators")
    public ResponseEntity filterOperator() {
        Map<String, Map<String, String>> filterOperators = filterJobService.getFilterOperators();
        return ResponseEntity.ok(filterOperators);
    }
}
