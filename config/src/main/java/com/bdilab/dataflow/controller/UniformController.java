package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.service.UniformService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * UniformController for all operator interface.
 *
 * @author Zunjing Chen
 * @date  2021-11-03
 */
public class UniformController {

  @Autowired
  UniformService uniformService;

  @PostMapping("/uniform")
  @ApiOperation(value = "Uniform Interface")
  public ResponseEntity unify(@RequestBody JSONObject requestData) {
    return ResponseEntity.ok(uniformService.analyze(requestData));
  }
}
