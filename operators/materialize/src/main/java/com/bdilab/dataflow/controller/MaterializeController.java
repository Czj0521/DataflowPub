package com.bdilab.dataflow.controller;


import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.service.MaterializeJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Materialize Operator Controller.

 * @author: wh
 * @create: 2021-10-27
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Materialize")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class MaterializeController {
  @Resource
  MaterializeJobService materializeJobService;

  @PostMapping("/materialize")
  @ApiOperation(value = "materialize")
  public ResponseEntity materialize(@Valid @RequestParam("operatorId") String operatorId) {
    return ResponseEntity.ok(materializeJobService.materialize(operatorId));
  }

  @DeleteMapping("/deleteMaterialize")
  @ApiOperation(value = "deleteMaterialize")
  public ResponseEntity deleteMaterialize(@Valid @RequestParam("materializeId") String materializeId) {
    return ResponseEntity.ok(materializeJobService.deleteMaterialize(materializeId));
  }
}
