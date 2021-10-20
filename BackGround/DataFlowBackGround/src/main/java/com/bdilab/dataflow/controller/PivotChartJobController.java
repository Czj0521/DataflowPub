package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.pivotchartjson.PivotChartResponseJson;
import com.bdilab.dataflow.service.PivotChartJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/16
 * PivotChart仪表盘-数据处理controller
 *
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "PivotChart仪表盘")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class PivotChartJobController {

    @Resource
    PivotChartJobService pivotChartJobService;

    @PostMapping("/pivotChart")
    @ApiOperation("图表仪盘")
    public ResponseEntity pivotChart(@RequestBody String inputJson){
        PivotChartResponseJson resultMap = pivotChartJobService.submitPivotChartJob(inputJson);
        return ResponseEntity.ok(resultMap);
    }
}
