package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.TransformationDesc;
import com.bdilab.dataflow.service.TransformationService;
import com.bdilab.dataflow.sql.generator.TransformationSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TransformationServiceImpl.
 *
 * @author Zunjing Chen
 * @date 2021-12-10
 **/
@Service
@Slf4j
public class TransformationServiceImpl implements TransformationService {

  @Autowired
  private ClickHouseManager clickHouseManager;

  @Override
  public List<Map<String, Object>> execute(TransformationDesc jobDescription) {
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode) {
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    TransformationDesc description = nodeDescription.toJavaObject(TransformationDesc.class);
    TransformationSqlGenerator sqlGenerator = new TransformationSqlGenerator(description);
    String sql = sqlGenerator.generateDataSourceSql();
    log.info("Transformation SQL:" + sql);
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    clickHouseManager.createView(tableName, sql);
    return null; // transformation do not need to show data
  }

}
