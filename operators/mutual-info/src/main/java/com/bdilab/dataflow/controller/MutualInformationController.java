package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.service.MutualInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author: Guo Yongqiang
 * @date: 2021/12/24 17:56
 * @version:
 */
@Slf4j
@Controller
@Api(tags = "Mutual Information Operator")
@RequestMapping(WebConstants.BASE_API_PATH + "/gluttony/mutual-information")
public class MutualInformationController {
  @Autowired
  private MutualInformationService mutualInformationService;

  @GetMapping("/features")
  @ApiOperation("Get accessible features of a dataset for mutual information operator.")
  public ResponseEntity accessibleFeatures(@ApiParam @RequestParam String[] dataSource) {
    List<Map<String, Object>> features = mutualInformationService.getAccessibleFeatures(dataSource);
    return ResponseEntity.ok(features);
  }
}
