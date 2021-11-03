package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.service.UniformService;
import java.util.List;
import java.util.Map;
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
  TransposeService transposeService;
  @Autowired
  TableJobService tableJobService;

  @Override
  public Object analyze(JSONObject json) {
    // todo: set the uniform structure for returned data
    String datasource = generateDataSourceRecursively(json);
    json.put("datasource", datasource);
    return executeJob(json);
  }

  private List<Map<String, Object>> executeJob(JSONObject json) {
    String jobType = json.getString("jobType");
    switch (jobType) {
      //todo: set other case when other operator is done
      case JobTypeConstants.TABLE_JOB:
        return tableJobService.execute(TableDescription.generateFromJson(json));
      default:
        throw new UncheckException("Unknown jobType to execute job");
    }
  }

  private String generateDataSourceRecursively(JSONObject requestData) {
    String jobType = requestData.getString("jobType");
    String datasource = requestData.get("datasource") instanceof String
        ? requestData.getString("datasource")
        : generateDataSourceRecursively(requestData.getJSONObject("datasource"));
    switch (jobType) {
      case JobTypeConstants.FILTER_JOB:
        //todo
      case JobTypeConstants.TABLE_JOB:
        //todo
      case JobTypeConstants.TRANSPOSE_JOB:
        TransposeDescription transposeDescription =
            TransposeDescription.generateFromJson(requestData);
        transposeDescription.setDataSource(datasource);
        // generate sql
        return transposeService.transpose(transposeDescription);
      default:
        throw new UncheckException("Unknown jobType to generate datasource");
    }
  }
}
