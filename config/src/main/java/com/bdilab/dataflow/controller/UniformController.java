package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.service.UniformService;
import com.bdilab.dataflow.service.WebSocketResolveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UniformController for all operator interface.
 *
 * @author Zunjing Chen
 * @date  2021-11-03
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Linkage")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/")
public class UniformController {
  @Autowired
  WebSocketResolveService socketResolveService;

//  @PostMapping("/linkage")
//  @ApiOperation(value = "联动接口")
//  public ResponseEntity linkage(@RequestBody JSONObject requestData) {
//    return ResponseEntity.ok(uniformService.analyze(requestData));
//  }

  @PostMapping("/linkage")
  @ApiOperation(value = "联动接口")
  public void linkage(@RequestBody JSONObject requestData) {
    socketResolveService.resolve(requestData.toJSONString());
  }
}
