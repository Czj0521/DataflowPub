package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.dto.jobinputjson.TableInputJson;
import com.bdilab.dataflow.dto.joboutputjson.TableOutputJson;
import com.bdilab.dataflow.operator.dto.jobinputjson.AbstractJobInputJson;
import com.bdilab.dataflow.service.impl.TableJobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @date 2021/8/28
 * master
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Table Operator")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class TableController {

    @Autowired
    TableJobServiceImpl tableJobService;

//    @GetMapping("/statistic")
//    @ApiOperation(value = "Get Table Structure")
//    public ResponseEntity getBaseTableProfiler(@RequestParam(value = "tableName") @ApiParam(value = "tableName") String columnName) {
//        return ResponseEntity.ok(tableJobService.getProfiler(columnName));
//    }
//
//    @GetMapping("/saveStatistic")
//    @ApiOperation(value = "Save Table Structure")
//    public ResponseEntity saveBaseTableProfiler(@RequestParam(value = "tableName") @ApiParam(value = "tableName") String tableName) {
//        tableJobService.saveTableConstruct(tableName);
//        return ResponseEntity.ok("save success");
//    }

    @PostMapping("/table")
    @ApiOperation(value = "Table Operator")
    public ResponseEntity table(@RequestBody TableInputJson tableInputJson) {
        List<Map<String, Object>> table = tableJobService.table(tableInputJson.getTableDescription());
        TableOutputJson tableOutputJson = new TableOutputJson();
        tableOutputJson.setOutputs(table);
        tableOutputJson.setJobStatus("JOB_FINISH");
        tableOutputJson.setRequestId(tableInputJson.getRequestId());
        tableOutputJson.setWorkspaceId(tableInputJson.getRequestId());
        return ResponseEntity.ok(tableOutputJson);
    }
}