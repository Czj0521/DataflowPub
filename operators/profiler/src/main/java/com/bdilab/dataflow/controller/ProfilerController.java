//package com.bdilab.dataflow.controller;
//
//
//import com.bdilab.dataflow.common.consts.WebConstants;
//import com.bdilab.dataflow.dto.ProfilerDescription;
//import com.bdilab.dataflow.service.impl.ProfilerServiceImpl;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * Profiler Controller.
//
// * @author: Yushaochao
// * @create: 2021-11-11
// **/
//@Slf4j
//@RestController
//@Api(tags = "Profiler Operator")
//@CrossOrigin
//@RequestMapping(value = WebConstants.BASE_API_PATH + "/operator")
//public class ProfilerController {
//  @Autowired
//  ProfilerServiceImpl profilerService;
//
//  @PostMapping(value = "/profiler")
//  @ApiOperation(value = "operator-profiler")
//  public ResponseEntity profiler(@RequestBody ProfilerDescription profilerDescription) {
//    return ResponseEntity.ok(profilerService.getProfiler(profilerDescription));
//  }
//}
