package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.EnumerationIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.RangeIndependentVariable;
import com.bdilab.dataflow.service.WhatIfServiceImpl;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WhatIfTest {
  @Resource
  WhatIfServiceImpl whatIfService;

  @Test
  void test(){
    WhatIfDescription whatIfDescription = new WhatIfDescription();
    whatIfDescription.setCollectors(new String[]{"avg(trans1)", "avg(trans2)", "avg(AQI)", "count(trans2)"});
    whatIfDescription.setDataSource(new String[]{"dataflow.airuuid"});
    whatIfDescription.setJobType("base");

    Expression expression = new Expression();
    DependentVariable dependentVariable1 = new DependentVariable("trans1", "($new1$ * 0.1) + AQI");
    DependentVariable dependentVariable2 = new DependentVariable("trans2", "$new2$ + AQI");
    expression.getDependentVariables().add(dependentVariable1);
    expression.getDependentVariables().add(dependentVariable2);
    BaseIndependentVariable integerEnumeration1 = new EnumerationIndependentVariable("$new1$", "0",new ArrayList<String>(){{
      this.add("2");
      this.add("4");
      this.add("6");
    }});
    BaseIndependentVariable integerEnumeration2 = new RangeIndependentVariable("$new2$", "0", "0", "100", "10");
    expression.getIndependentVariables().add(integerEnumeration1);
    expression.getIndependentVariables().add(integerEnumeration2);

    DagNode dagNode = new DagNode(new DagNodeInputDto("whatIf_Test", "what_if", JSONObject.toJSON(whatIfDescription)));
    whatIfService.saveToClickHouse(dagNode, expression);
  }
}
