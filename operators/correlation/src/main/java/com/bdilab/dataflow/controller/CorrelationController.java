package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobDescription.CorrelationDescription;
import com.bdilab.dataflow.service.CorrelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CorrelationController.
 *
 * @author Liu Pan
 * @2021-12-23
 **/
@Slf4j
@RestController
@Api(tags = "Operator")
@CrossOrigin
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/operator")
public class CorrelationController {
  @Autowired
  CorrelationService correlationService;

  @PostMapping("/correlation")
  public ResponseEntity correlation(@RequestBody @ApiParam(value = "correlation任务描述")
                                        CorrelationDescription description) {
    return ResponseEntity.ok(correlationService.correlation(description));
  }
}
