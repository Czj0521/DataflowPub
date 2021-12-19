package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.EnumerationIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.RangeIndependentVariable;
import com.bdilab.dataflow.sql.generator.WhatIfBaseSqlGenerator;
import com.bdilab.dataflow.sql.generator.WhatIfLinkSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
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
//    System.out.println(sqlGenerator.generate());
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


    Expression expression = new Expression();
    DependentVariable dependentVariable1 = new DependentVariable("trans1", "($new1$ * 0.1) + AQI");
    DependentVariable dependentVariable2 = new DependentVariable("trans2", "$new2$+ AQI");
    expression.getDependentVariables().add(dependentVariable1);
    expression.getDependentVariables().add(dependentVariable2);
    BaseIndependentVariable integerEnumeration1 = new EnumerationIndependentVariable<Integer>("$new1$", 0,new ArrayList<Integer>(){{
      this.add(2);
      this.add(4);
      this.add(6);
    }});
    BaseIndependentVariable integerEnumeration2 = new RangeIndependentVariable<Integer>("$new2$", 0, 0, 100, 20);
    expression.getIndependentVariables().add(integerEnumeration1);
    expression.getIndependentVariables().add(integerEnumeration2);
    whatIfService.execute(whatIfDescription, expression);


    String d = "{\"jobType\":\"base\", \"dataSource\":[\"air\"], \"collectors\":[\"count(AQI)\"]}";
//    String d = "{\"jobType\":\"base\", \"dataSource\":[\"air\"], \"collectors\":[\"count(AQI)\"], \"defaultValue\":1, \"possibleValues\":[2, 4, 6]}";
    System.out.println(d);
    JSONObject jsonObject = JSONObject.parseObject(d);
//    whatIfService.saveToClickHouse(new DagNode(new DagNodeInputDto("id", "type", jsonObject)), integerEnumeration);
    whatIfService.execute(whatIfDescription);
  }
}
