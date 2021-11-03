package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.FilterJobService;
import com.bdilab.dataflow.service.MaterializeJobService;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.service.UniformService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




/**
 * Uniform service for all job .
 *
 * @author Zunjing Chen
 * @date 2021-11-03
 **/
@Service
public class UniformServiceImpl implements UniformService {

  @Autowired
  TransposeServiceImpl transposeServiceImpl;
  @Autowired
  TableJobService tableJobService;
  @Resource
  MaterializeJobService materializeJobService;
  @Resource
  FilterJobService filterJobService;

  @Override
  public Object analyze(JSONObject json) {
    Object outputs;
    String operatorType = json.getString("operatorType");
    switch (operatorType) {
      //todo: set other case when other operator is done
      case JobTypeConstants.TABLE_JOB:
        JSONObject tableDescription = json.getJSONObject("tableDescription");
        String datasource = tableDescription.get("datasource") instanceof String
            ? tableDescription.getString("datasource")
            : generateDataSourceRecursively(tableDescription.getJSONObject("datasource"));
        tableDescription.put("datasource", datasource);
        outputs = tableJobService.execute(TableDescription.generateFromJson(tableDescription));
        break;
      case JobTypeConstants.MATERIALIZE_JOB:
        JSONObject materializeDescription = json.getJSONObject("materializeDescription");
        String subTableSql = generateDataSourceRecursively(
            materializeDescription.getJSONObject("materializedOperator"));
        outputs = materializeJobService.materialize(subTableSql);
        break;
      default:
        throw new UncheckException("Unknown jobType to execute job");
    }
    JSONObject result = new JSONObject();
    result.put("jobStatus", "JOB_FINISH");
    result.put("requestId", json.getString("requestId"));
    result.put("workspaceId", json.getString("workspaceId"));
    result.put("outputs", outputs);
    return result;
  }

  //  private List<Map<String, Object>> executeJob(JSONObject json) {
  //    String jobType = json.getString("jobType");
  //    switch (jobType) {
  //      //todo: set other case when other operator is done
  //      case JobTypeConstants.TABLE_JOB:
  //        return tableJobService.execute(TableDescription.generateFromJson(json));
  //      default:
  //        throw new UncheckException("Unknown jobType to execute job");
  //    }
  //  }

  private String generateDataSourceRecursively(JSONObject requestData) {
    String jobType = requestData.getString("jobType");
    String datasource = requestData.get("datasource") instanceof String
        ? requestData.getString("datasource")
        : generateDataSourceRecursively(requestData.getJSONObject("datasource"));
    switch (jobType) {
      case JobTypeConstants.FILTER_JOB:
        FilterDescription filterDescription =
            FilterDescription.generateFromJson(requestData);
        filterDescription.setDataSource(datasource);
        return filterJobService.generateDataSourceSql(filterDescription);
      case JobTypeConstants.TABLE_JOB:
        //todo
      case JobTypeConstants.TRANSPOSE_JOB:
        TransposeDescription transposeDescription =
            TransposeDescription.generateFromJson(requestData);
        transposeDescription.setDataSource(datasource);
        // generate sql
        return transposeServiceImpl.generateDataSourceSql(transposeDescription);
      default:
        throw new UncheckException("Unknown jobType to generate datasource");
    }
  }
}
