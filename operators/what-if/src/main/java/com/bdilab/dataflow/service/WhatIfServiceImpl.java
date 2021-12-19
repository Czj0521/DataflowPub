package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.EnumerationIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.RangeIndependentVariable;
import com.bdilab.dataflow.sql.generator.WhatIfBaseSqlGenerator;
import com.bdilab.dataflow.sql.generator.WhatIfLinkSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WhatIfServiceImpl implements OperatorService<WhatIfDescription> {
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Resource
  ClickHouseManager clickHouseManager;

  @Override
  public List<Map<String, Object>> execute(WhatIfDescription jobDescription) {
    return null;
  }

  public String generateSql(WhatIfDescription whatIfDescription, Expression extendMessage) {
    WhatIfBaseSqlGenerator sqlGenerator;
    if(extendMessage == null){
      sqlGenerator = new WhatIfBaseSqlGenerator(whatIfDescription);
    } else {
      sqlGenerator = new WhatIfLinkSqlGenerator(whatIfDescription, extendMessage);
    }
    return sqlGenerator.generate();
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    WhatIfDescription whatIfDescription =
        ((JSONObject) dagNode.getNodeDescription()).toJavaObject(WhatIfDescription.class);
    String sql = generateSql(whatIfDescription, (Expression) extendMessage);
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    clickHouseManager.createView(tableName, sql);
    return clickHouseJdbcUtils.queryForList(sql);
  }
}
