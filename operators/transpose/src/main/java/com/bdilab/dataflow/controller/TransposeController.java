package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.impl.TransposeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "transpose")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class TransposeController {
    @Autowired
    TransposeServiceImpl transposeService;
    @PostMapping("/transpose")
    @ApiOperation(value = "transpose")
    public ResponseEntity transpose(@Valid @RequestBody TransposeDescription transposeDescription)  {
        return ResponseEntity.ok(transposeService.transpose(transposeDescription));
    }
}
