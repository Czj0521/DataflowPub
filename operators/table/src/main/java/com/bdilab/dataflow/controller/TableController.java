package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.common.consts.WebConstants;
import com.bdilab.dataflow.dto.jobinputjson.TableInputJson;
import com.bdilab.dataflow.dto.joboutputjson.TableOutputJson;
import com.bdilab.dataflow.service.impl.TableJobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * TableController.

 * @author tm
 * @version 1.0
 * @date 2021/8/28
 */
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "Table Operator")
@RequestMapping(value = WebConstants.BASE_API_PATH + "/gluttony/job")
public class TableController {

  @Autowired
  TableJobServiceImpl tableJobService;

  /**
   * Table Operator.
   */
  @PostMapping("/table")
  @ApiOperation(value = "Table Operator")
  public ResponseEntity table(@RequestBody TableInputJson tableInputJson) {
    List<Map<String, Object>> table = tableJobService.execute(tableInputJson.getTableDescription());
    TableOutputJson tableOutputJson = new TableOutputJson();
    tableOutputJson.setOutputs(table);
    tableOutputJson.setJobStatus("JOB_FINISH");
    tableOutputJson.setRequestId(tableInputJson.getRequestId());
    tableOutputJson.setWorkspaceId(tableInputJson.getRequestId());
    return ResponseEntity.ok(tableOutputJson);
  }
}