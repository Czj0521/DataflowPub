package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
            JSON.toJavaObject(materializedOperator, TableDescription.class);
        datasourceSql = new TableSqlGenerator(tableDescription).generateDataSourceSql();
        break;
      case "transpose":
        TransposeDescription transposeDescription =
            JSON.toJavaObject(materializedOperator, TransposeDescription.class);
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
    StringBuilder sbSql = new StringBuilder();
    String name = com.bdilab.dataflow.common.consts.CommonConstants.DATABASE
        + "." + SqlParseUtils.getUuid32();
    sbSql.append("CREATE VIEW ").append(name).append(" AS ")
        .append("(").append(datasourceSql).append(")");
    String sql = new String(sbSql);
    URLEncoder urlEncoder = new URLEncoder();
    sql = urlEncoder.encode(sql, Charset.defaultCharset());
    log.info("Materialize Sql: {}", sql);
    while (ClickHouseHttpUtils.sendPost(httpPrefix + sql).length() > 0) {
      log.info("Error: View name already exists, try again.");
    }
    log.info("Materialize job: {} has been created", name);
    Map<String, String> metadata = tableMetadataServiceImpl.metadataFromDatasource(name);
    MaterializeOutputJson materializeOutputJson =
        new MaterializeOutputJson();
    JSONObject outputs = new JSONObject();
    outputs.put("metadata", metadata);
    outputs.put("subTableId", name);
    materializeOutputJson.setOutputs(outputs);
    materializeOutputJson.setJobStatus("JOB_FINISH");
    materializeOutputJson.setRequestId(materializeInputJson.getRequestId());
    materializeOutputJson.setWorkspaceId(materializeInputJson.getWorkspaceId());
    return materializeOutputJson;
  }

  @Override
  public String deleteSubTable(String subTableId) {
    clickHouseJdbcUtils.execute("DROP VIEW " + subTableId);
    return "success";
  }
}
