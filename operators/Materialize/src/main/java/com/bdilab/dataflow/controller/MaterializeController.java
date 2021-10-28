package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.service.MaterializeJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Materialize Operator Controller.
 * @author: wh
 * @create: 2021-10-27
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Materialize Operator")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class MaterializeController {
    @Resource
    MaterializeJobService materializeJobService;

    @PostMapping("/materialize")
    @ApiOperation(value = "materialize")
    public ResponseEntity table(@RequestBody MaterializeInputJson materializeInputJson) {
        return ResponseEntity.ok(materializeJobService.materialize(materializeInputJson));
    }
}
