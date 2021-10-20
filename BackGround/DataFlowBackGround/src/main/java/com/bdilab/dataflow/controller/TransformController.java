package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.impl.TransposeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-28
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "transform控件")
public class TransformController {
    @Autowired
    TransposeServiceImpl transposeService;
    @PostMapping("/transform")
    @ApiOperation(value = "transform控件")
    public ResponseEntity transpose(@RequestBody TransposeDescription transposeDescription)  {
        return ResponseEntity.ok("");
    }
}
