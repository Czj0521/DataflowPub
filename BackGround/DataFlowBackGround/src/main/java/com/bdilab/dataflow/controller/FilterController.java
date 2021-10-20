package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.service.impl.FilterJobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-22
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "filter控件")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class FilterController {
    @Autowired
    FilterJobServiceImpl filterJobService;

    /**
     * 根据过滤操作创建视图，返回视图id，并不产生实际数据。等到其作为数据源拖拽出，再展示（调用table api）
     *
     * @return datasource id
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/filter")
    @ApiOperation(value = "filter控件")
    public ResponseEntity filter(@RequestBody FilterDescription filterDescription)  {
        return ResponseEntity.ok(filterJobService.filter(filterDescription));
    }
}
