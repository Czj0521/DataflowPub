package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.independvariable.BaseVariable;
import com.bdilab.dataflow.dto.pojo.independvariable.EnumerationVariable;
import com.bdilab.dataflow.sql.generator.WhatIfBaseSqlGenerator;
import com.bdilab.dataflow.sql.generator.WhatIfLinkSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WhatIfServiceImpl implements OperatorService<WhatIfDescription> {
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Override
  public List<Map<String, Object>> execute(WhatIfDescription jobDescription) {
    return null;
  }

  public List<Map<String, Object>> execute(WhatIfDescription whatIfDescription, Expression extendMessage) {
    String sql = "";
    WhatIfBaseSqlGenerator sqlGenerator;
    if(extendMessage == null){
      sqlGenerator = new WhatIfBaseSqlGenerator(whatIfDescription);
    } else {
      sqlGenerator = new WhatIfLinkSqlGenerator(whatIfDescription, extendMessage);
    }
    System.out.println(sqlGenerator.generate());
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Object extendMessage) {
    WhatIfDescription whatIfDescription =
        ((JSONObject) dagNode.getNodeDescription()).toJavaObject(WhatIfDescription.class);
    execute(whatIfDescription, (Expression) extendMessage);
    return null;
  }

  public static void main(String[] args) {
    WhatIfDescription whatIfDescription = new WhatIfDescription();
    whatIfDescription.setCollectors(new String[]{"count(AQI)"});
    whatIfDescription.setDataSource(new String[]{"air"});
    whatIfDescription.setJobType("base");
    WhatIfServiceImpl whatIfService = new WhatIfServiceImpl();
    BaseVariable integerEnumeration = new EnumerationVariable<Integer>("$a$", 0,new ArrayList<Integer>(){{
      this.add(2);
      this.add(4);
      this.add(6);
    }});
    System.out.println(integerEnumeration);
    Expression expression = new Expression();
    expression.setExpression("($new1$ * 0.1) + AQI + $new2$");
    expression.setDependentVariable("transformation 1");
    String d = "{\"jobType\":\"base\", \"dataSource\":[\"air\"], \"collectors\":[\"count(AQI)\"]}";
//    String d = "{\"jobType\":\"base\", \"dataSource\":[\"air\"], \"collectors\":[\"count(AQI)\"], \"defaultValue\":1, \"possibleValues\":[2, 4, 6]}";
    System.out.println(d);
    JSONObject jsonObject = JSONObject.parseObject(d);
//    whatIfService.saveToClickHouse(new DagNode(new DagNodeInputDto("id", "type", jsonObject)), integerEnumeration);
    whatIfService.execute(whatIfDescription);
  }
}
