package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.TransformationDescription;
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
  public List<Map<String, Object>> execute(TransformationDescription jobDescription) {
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMsg) {
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    TransformationDescription description = nodeDescription
        .toJavaObject(TransformationDescription.class);
    TransformationSqlGenerator sqlGenerator = new TransformationSqlGenerator(description);
    String sql = sqlGenerator.generateDataSourceSql();
    log.info("Transformation SQL:" + sql);
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    try {
      clickHouseManager.createView(tableName, sql);
    } catch (Exception e) {
      throw new UncheckException(ExceptionMsgEnum.TRANSFORMATION_ERROR.getMsg());
    }
    return null; // transformation do not need to show data
  }
}
