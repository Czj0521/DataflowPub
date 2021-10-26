package com.bdilab.dataflow.controller;


import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.JoinJson;
import com.bdilab.dataflow.service.impl.JoinServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Yu Shaochao
 * @create: 2021-10-24
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "join控件")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class JoinController {
    @Autowired
    JoinServiceImpl joinService;
    @PostMapping("/join")
    @ApiOperation(value = "join控件")
    public ResponseEntity filter(@RequestBody JoinJson joinJson){
        return ResponseEntity.ok(joinService.join(joinJson));
    }
}
