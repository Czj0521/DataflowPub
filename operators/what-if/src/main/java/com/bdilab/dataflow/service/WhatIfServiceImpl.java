package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.sql.generator.WhatIfBaseSqlGenerator;
import com.bdilab.dataflow.sql.generator.WhatIfLinkSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



/**
 * What-If job ServiceImpl.

 * @author: wh
 * @create: 2021-11-20
 */
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

  /**
   * Generate Sql for Base or Link.
   *
   * @param whatIfDescription whatIfDescription
   * @param extendMessage extendMessage
   * @return sql
   */
  public String generateSql(WhatIfDescription whatIfDescription, Expression extendMessage) {
    WhatIfBaseSqlGenerator sqlGenerator;
    if (extendMessage == null) {
      sqlGenerator = new WhatIfBaseSqlGenerator(whatIfDescription);
    } else {
      sqlGenerator = new WhatIfLinkSqlGenerator(whatIfDescription, extendMessage);
    }
    String sql = sqlGenerator.generate();
    log.debug("What If: {}", sql);
    return sql;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    WhatIfDescription whatIfDescription =
        ((JSONObject) dagNode.getNodeDescription()).toJavaObject(WhatIfDescription.class);
    String sql = generateSql(whatIfDescription, (Expression) extendMessage);
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    clickHouseManager.createView(tableName, sql);
    List<Map<String, Object>> res = new ArrayList<>();
    res.add(new HashMap<String, Object>(1){{
      this.put("status", "success");
    }});
    return res;
  }
}
