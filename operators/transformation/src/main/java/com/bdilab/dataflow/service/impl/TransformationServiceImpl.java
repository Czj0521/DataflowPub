package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.TransformationDesc;
import com.bdilab.dataflow.service.TransformationService;
import com.bdilab.dataflow.sql.generator.TransformationSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * TransformationServiceImpl.
 *
 * @author Zunjing Chen
 * @date 2021-12-10
 **/
public class TransformationServiceImpl implements TransformationService {

  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  private ClickHouseManager clickHouseManager;

  @Override
  public List<Map<String, Object>> execute(TransformationDesc jobDescription) {
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode,
      Map<Integer, StringBuffer> preFilterMap) {
    // add filter
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    String filter0 = nodeDescription.getString("filter");
    if (!StringUtils.isEmpty(filter0)) {
      nodeDescription.put("filter", filter0 + " AND " + preFilterMap.get(0).toString());
    } else {
      nodeDescription.put("filter", preFilterMap.get(0).toString());
    }
    TransformationDesc description = nodeDescription.toJavaObject(TransformationDesc.class);
    // save result to ck
    TransformationSqlGenerator sqlGenerator = new TransformationSqlGenerator(description);
    String sql = sqlGenerator.generateDataSourceSql();
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    clickHouseManager.createView(tableName, sql);
    return null; // transformation do not need to show data
  }

}
