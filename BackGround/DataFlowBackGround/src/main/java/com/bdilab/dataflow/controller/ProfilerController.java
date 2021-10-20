package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSON;
import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.profilerjson.ProfilerResponseJson;
import com.bdilab.dataflow.service.impl.ProfilerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author wjh
 */

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Profiler")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class ProfilerController {
    @Autowired
    ProfilerServiceImpl profilerService;
    @PostMapping("/profiler")
    @ApiOperation(value = "根据表名和列名获取详细信息")
    public ResponseEntity getBaseTableProfiler(@RequestBody String inputJson) {
        ProfilerResponseJson profiler = profilerService.getProfiler(inputJson);
        return ResponseEntity.ok(JSON.toJSONString(profiler));
    }
}
