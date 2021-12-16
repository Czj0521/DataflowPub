package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.sql.generator.WhatIfBaseSqlGenerator;
import com.bdilab.dataflow.sql.generator.WhatIfLinkSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class WhatIfServiceImpl implements OperatorService<WhatIfDescription> {
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Override
  public List<Map<String, Object>> execute(WhatIfDescription whatIfDescription) {
    String sql = "";
    WhatIfBaseSqlGenerator sqlGenerator;
    whatIfDescription.setExpression("adwadw");
    if(whatIfDescription.getExpression() == null){
      sqlGenerator = new WhatIfBaseSqlGenerator(whatIfDescription);
    } else {
      sqlGenerator = new WhatIfLinkSqlGenerator(whatIfDescription);
    }
    System.out.println(sqlGenerator.generate());
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode) {
    WhatIfDescription whatIfDescription =
        ((JSONObject) dagNode.getNodeDescription()).toJavaObject(WhatIfDescription.class);
    execute(whatIfDescription);
    return null;
  }

  public static void main(String[] args) {
    WhatIfDescription whatIfDescription = new WhatIfDescription();
    whatIfDescription.setCollectors(new String[]{"count(AQI)"});
    whatIfDescription.setDataSource(new String[]{"air"});
    whatIfDescription.setJobType("base");
    WhatIfServiceImpl whatIfService = new WhatIfServiceImpl();

    whatIfService.execute(whatIfDescription);
  }
}
