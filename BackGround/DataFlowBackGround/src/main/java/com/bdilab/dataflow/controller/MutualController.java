package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.MutualDescription;
import com.bdilab.dataflow.service.impl.MutualServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 互信息求解
 * @author:zhb
 * @createTime:2021/9/26 14:25
 */

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Mutual控件")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class MutualController {
    @Autowired
    private MutualServiceImpl mutualService;
    @PostMapping("/mutual")
    @ApiOperation(value = "获取互信息mutual")
    public ResponseEntity getMutual(@RequestBody MutualDescription mutualDescription) {
        return ResponseEntity.ok(mutualService.getMutual(mutualDescription));
    }
}
