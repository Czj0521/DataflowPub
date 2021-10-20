package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.service.impl.TableMetadataServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-22
 * @description:
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "元数据服务")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/metadata")
public class MetaDataController {
    @Autowired
    TableMetadataServiceImpl tableMetadataService;

    @GetMapping("/datasource")
    @ApiOperation(value = "根据表名返回元数据")
    public ResponseEntity filter(@RequestParam @ApiParam(value = "表名") String datasource) {
        return ResponseEntity.ok(tableMetadataService.metadataFromDatasource(datasource));
    }

}
