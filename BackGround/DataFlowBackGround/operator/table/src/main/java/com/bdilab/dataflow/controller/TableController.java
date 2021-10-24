package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.service.impl.TableJobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @author
 * @version 1.0
 * @date 2021/8/28
 * Table仪表盘-数据处理controller
 * master
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Table控件")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class TableController {

    @Autowired
    TableJobServiceImpl tableJobService;

    @GetMapping("/statistic")
    @ApiOperation(value = "表结构")
    public ResponseEntity getBaseTableProfiler(@RequestParam(value = "tableName") @ApiParam(value = "表名") String columnName) {
        return ResponseEntity.ok(tableJobService.getProfiler(columnName));
    }

    @GetMapping("/savastatistic")
    @ApiOperation(value = "保存表结构")
    public ResponseEntity saveBaseTableProfiler(@RequestParam(value = "tableName") @ApiParam(value = "表名") String tableName) {
        tableJobService.saveTableConstruct(tableName);
        return ResponseEntity.ok("save success");
    }


    @PostMapping("/table")
    @ApiOperation(value = "table控件")
    public ResponseEntity table(@RequestBody TableDescription tableDescription) throws UnsupportedEncodingException {
        return ResponseEntity.ok(tableJobService.table(tableDescription));
    }
}