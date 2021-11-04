package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.dto.jobdescription.MaterializeDescription;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.dto.joboutputjson.MaterializeOutputJson;
import com.bdilab.dataflow.service.MaterializeJobService;
import com.bdilab.dataflow.sql.generator.TableSqlGenerator;
import com.bdilab.dataflow.utils.SqlParseUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.nio.charset.Charset;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Materialize Job Service Impl.

 * @author: wh
 * @create: 2021-10-28
 */
@Service
@Slf4j
public class MaterializeJobServiceImpl implements MaterializeJobService {
  @Value("${clickhouse.http.url}")
  private String httpPrefix;
  @Resource
  TransposeServiceImpl transposeServiceImpl;
  @Resource
  JoinServiceImpl joinServiceImpl;
  @Resource
  TableMetadataServiceImpl tableMetadataServiceImpl;
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Override
  public MaterializeOutputJson materialize(MaterializeInputJson materializeInputJson) {
    MaterializeDescription materializeDescription =
        materializeInputJson.getMaterializeDescription();
    JSONObject materializedOperator = materializeDescription.getMaterializedOperator();
    String datasourceSql;
    switch (materializeDescription.getMaterializedType()) {
      case "table":
        TableDescription tableDescription =
            TableDescription.generateFromJson(materializedOperator);
        datasourceSql = new TableSqlGenerator(tableDescription).generateDataSourceSql();
        break;
      case "transpose":
        TransposeDescription transposeDescription =
            TransposeDescription.generateFromJson(materializedOperator);
        datasourceSql = transposeServiceImpl.generateDataSourceSql(transposeDescription);
        break;
      case "join":
        JoinDescription joinDescription =
            com.bdilab.dataflow.dto.JoinDescription.generateFromJson(materializedOperator);
        datasourceSql = joinServiceImpl.generateDataSourceSql(joinDescription);
        break;
      default:
        throw new RuntimeException("Wrong materialized type!");
    }
    JSONObject outputs = materialize(datasourceSql);
    MaterializeOutputJson materializeOutputJson =
        new MaterializeOutputJson();
    materializeOutputJson.setOutputs(outputs);
    materializeOutputJson.setJobStatus("JOB_FINISH");
    materializeOutputJson.setRequestId(materializeInputJson.getRequestId());
    materializeOutputJson.setWorkspaceId(materializeInputJson.getWorkspaceId());
    return materializeOutputJson;
  }

  @Override
  public JSONObject materialize(String subTableSql) {
    StringBuilder sbName = new StringBuilder();
    sbName.append(CommonConstants.DATABASE).append(".")
        .append(JobTypeConstants.MATERIALIZE_JOB).append("_").append(SqlParseUtils.getUuid32());
    String name = new String(sbName);
    StringBuilder sbSql = new StringBuilder();
    sbSql.append("CREATE VIEW ").append(name).append(" AS ")
        .append("(").append(subTableSql).append(")");
    String sql = new String(sbSql);
    log.info("Materialize Sql: {}", sql);
    URLEncoder urlEncoder = new URLEncoder();
    sql = urlEncoder.encode(sql, Charset.defaultCharset());
    while (ClickHouseHttpUtils.sendPost(httpPrefix + sql).length() > 0) {
      log.info("Error: View name already exists, try again.");
    }
    log.info("Materialize job: {} has been created", name);
    Map<String, String> metadata = tableMetadataServiceImpl.metadataFromDatasource(name);
    JSONObject outputs = new JSONObject();
    outputs.put("metadata", metadata);
    outputs.put("subTableId", name);
    return outputs;
  }

  @Override
  public String deleteSubTable(String subTableId) {
    clickHouseJdbcUtils.execute("DROP VIEW " + subTableId);
    return "success";
  }
}
