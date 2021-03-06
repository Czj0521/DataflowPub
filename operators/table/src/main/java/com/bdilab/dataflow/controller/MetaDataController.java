package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.service.impl.TableMetadataServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MetaData Controller.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-22
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "MetaData Service")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/metadata")
public class MetaDataController {
  @Autowired
  TableMetadataServiceImpl tableMetadataService;

  @GetMapping("/datasource")
  @ApiOperation(value = "Returns metadata based on the table name")
  public ResponseEntity filter(@RequestParam @ApiParam(value = "table name") String datasource) {
    return ResponseEntity.ok(tableMetadataService.metadataFromDatasource(datasource));
  }

  @GetMapping("/getTables")
  @ApiOperation(value = "Get all table names")
  public ResponseEntity getTableName() {
    return ResponseEntity.ok(tableMetadataService.getTableName());
  }
}
