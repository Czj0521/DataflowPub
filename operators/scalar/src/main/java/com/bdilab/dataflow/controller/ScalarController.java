//package com.bdilab.dataflow.controller;
//
//import com.bdilab.dataflow.common.consts.WebConstants;
//import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
//import com.bdilab.dataflow.dto.jobinputjson.ScalarInputJson;
//import com.bdilab.dataflow.dto.joboutputjson.ScalarOutputJson;
//import com.bdilab.dataflow.service.ScalarService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author: Guo Yongqiang
// * @date: 2021/11/13 15:33
// * @version:
// */
//
//@Slf4j
//@RestController
//@CrossOrigin
//@Api(tags = "Scalar Operator")
//@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
//public class ScalarController {
//
//  @Autowired
//  ScalarService scalarService;
//
//  @PostMapping("/scalar")
//  @ApiOperation(value = "scalar")
//  public ResponseEntity scalar(@RequestBody ScalarInputJson scalarInputJson) {
//    ScalarDescription scalarDescription = scalarInputJson.getScalarDescription();
//    ScalarOutputJson scalarOutputJson = new ScalarOutputJson();
//    scalarOutputJson.setJobStatus("JOB_FINISH");
//    scalarOutputJson.setValue(scalarService.execute(scalarDescription).get(0).get("scalar"));
//    scalarOutputJson.setRequestId(scalarInputJson.getRequestId());
//    scalarOutputJson.setWorkspaceId(scalarInputJson.getWorkspaceId());
//
//    return ResponseEntity.ok(scalarOutputJson);
//  }
//}
