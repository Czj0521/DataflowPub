package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import com.bdilab.dataflow.utils.sql.FilterSQLGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Zunjing Chen
 * @create: 2021-10-09
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "统一接口")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class UnifyController {
    @PostMapping("/unify")
    @ApiOperation(value = "transpose控件")
    public ResponseEntity unify(@RequestBody JSONObject requestData) {
        log.info(requestData.toJSONString());
        return ResponseEntity.ok("");
    }

    String generateDataSource(JSONObject requestData) {
        String jobType = requestData.getString("jobType");
        switch (jobType) {
            case JobTypeConstants.FILTER_JOB:
                FilterDescription jobDescription = FilterDescription.generateFromJson(requestData);
                String datasource = requestData.get("datasource") instanceof String ? requestData.getString("datasource"): generateDataSource(requestData.getJSONObject("dataSource"));
                jobDescription.setDataSource(datasource);
                return new FilterSQLGenerator(jobDescription, SQLParseUtils.getUUID32()).generate();
            case JobTypeConstants.TABLE_JOB:
            case JobTypeConstants.TRANSFORM_JOB:
            case JobTypeConstants.TRANSPOSE_JOB:
            case JobTypeConstants.PROFILER_JOB:
                return requestData.get("datasource") instanceof String ? requestData.getString("datasource"): generateDataSource(requestData.getJSONObject("dataSource"));
            default:
                throw  new UncheckException("Unknown Job Type");
        }
    }
}
