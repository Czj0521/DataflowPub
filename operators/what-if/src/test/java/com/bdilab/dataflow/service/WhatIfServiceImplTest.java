package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.EnumerationIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.RangeIndependentVariable;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import java.util.ArrayList;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * What-If: Test WhatIfServiceImpl.
 *
 * @author: wh
 * @create: 2021-12-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class WhatIfServiceImplTest {
  @Resource
  WhatIfServiceImpl whatIfService;
  @Resource
  ClickHouseManager clickHouseManager;

  WhatIfDescription whatIfDescription = new WhatIfDescription();
  Expression expression = new Expression();

  @BeforeEach
  public void init(){
    try {
      whatIfDescription.setCollectors(new String[]{"avg(trans1)", "avg(trans2)", "avg(AQI)", "count(trans2)"});
      whatIfDescription.setDataSource(new String[]{"dataflow.airuuid"});
      whatIfDescription.setJobType("base");

      DependentVariable dependentVariable1 = new DependentVariable("trans1", "($new1$ * 0.1) + AQI");
      DependentVariable dependentVariable2 = new DependentVariable("trans2", "$new2$ + AQI + trans1");
      expression.getDependentVariables().add(dependentVariable1);
      expression.getDependentVariables().add(dependentVariable2);

      BaseIndependentVariable independentVariable1 = new EnumerationIndependentVariable("$new1$", "0",new ArrayList<String>(){{
        this.add("2");
        this.add("4");
        this.add("6");
      }});
      BaseIndependentVariable independentVariable2 = new RangeIndependentVariable("$new2$", "0", "0", "100", "10");
      expression.getIndependentVariables().add(independentVariable1);
      expression.getIndependentVariables().add(independentVariable2);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGenerateSql() {
    String baseSql = whatIfService.generateSql(whatIfDescription, null);

    if(("SELECT avg(trans1),avg(trans2),avg(AQI),count(trans2) " +
        "FROM dataflow.airuuid")
        .equals(baseSql)){
      log.info("Test - Pass： testGenerateSql_base_sql.");
    } else {
      throw new RuntimeException("Test - Error： testGenerateSql - Generate base sql error !");
    }

    String linkedSql = whatIfService.generateSql(whatIfDescription, expression);
    if(("WITH arrayJoin([2,4,6]) as `$new1$`," +
        "arrayJoin([0.0,10.0,20.0,30.0,40.0,50.0,60.0,70.0,80.0,90.0]) as `$new2$`," +
        "((`$new1$` * 0.1) + AQI) as trans1," +
        "(`$new2$` + AQI + trans1) as trans2 " +
        "SELECT `$new1$`,`$new2$`,avg(trans1),avg(trans2),avg(AQI),count(trans2)  " +
        "FROM dataflow.airuuid " +
        "GROUP BY `$new1$`,`$new2$`")
        .equals(linkedSql)){
      log.info("Test - Pass： testGenerateSql_linked_sql.");
    } else {
      throw new RuntimeException("Test - Error： testGenerateSql - Generate linked sql error !");
    }

  }

  @Test
  public void testSaveToClickHouse() {
    DagNode dagNode = new DagNode(new DagNodeInputDto("whatIf_Test", "what_if", JSONObject.toJSON(whatIfDescription)));
    try{
      whatIfService.saveToClickHouse(dagNode, expression);
      clickHouseManager.deleteTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + "whatIf_Test");
    } catch (Exception e) {
      throw new RuntimeException("Test - Error： testSaveToClickHouse !\n" + e.getMessage());
    }
    log.info("Test - Pass： testSaveToClickHouse.");
  }
}