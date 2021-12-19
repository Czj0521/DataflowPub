package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.sql.generator.TransposeSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class includes methods for querying column and transpose operator.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Service
public class TransposeServiceImpl implements TransposeService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  private ClickHouseManager clickHouseManager;
  /**
   * query column's value.
   *
   * @param transposeDescription transpose's dto
   * @return column value list
   */
  private List<String> columnValues(TransposeDescription transposeDescription) {
    String column = transposeDescription.getColumn();
    String datasource = transposeDescription.getDataSource()[0];
    String sql = "SELECT distinct(" + column + ") FROM " + datasource;
    return clickHouseJdbcUtils.queryForStrList(sql);
  }

  @Override
  public String transpose(TransposeDescription transposeDescription) {
    return new TransposeSqlGenerator(transposeDescription, columnValues(transposeDescription))
        .generate();
  }

  public String generateDataSourceSql(TransposeDescription transposeDescription) {
    return new TransposeSqlGenerator(transposeDescription, columnValues(transposeDescription))
        .generateDataSourceSql();
  }

  @Override
  public List<Map<String, Object>> execute(TransposeDescription jobDescription) {
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    // add filter
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    TransposeDescription description = nodeDescription.toJavaObject(TransposeDescription.class);
    // save result to ck
    TransposeSqlGenerator sqlGenerator = new TransposeSqlGenerator(description,columnValues(description));
    String sql = sqlGenerator.generateDataSourceSql();
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    clickHouseManager.createView(tableName, sql);
    return null;
  }
}
