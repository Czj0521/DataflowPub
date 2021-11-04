package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TransposeController.
 *
 * @author Zunjing Chen
 * @2021-10-31
 **/
@Slf4j
@RestController
@Api(tags = "Operator")
@CrossOrigin
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/operator")
public class TransposeController {
  @Autowired
  TransposeService transposeService;

  @PostMapping("/transpose")
  @ApiOperation(value = "transpose任务接口，只返回sql")
  public ResponseEntity transpose(@RequestBody @ApiParam(value = "transpose任务描述")
                                   TransposeDescription description) {
    return ResponseEntity.ok(transposeService.transpose(description));
  }

}
