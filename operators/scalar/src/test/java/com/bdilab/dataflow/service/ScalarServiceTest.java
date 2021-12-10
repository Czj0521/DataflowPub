package com.bdilab.dataflow.service;

import com.bdilab.dataflow.ScalarTestApplication;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.dto.jobinputjson.ScalarInputJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 17:53
 * @version:
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScalarTestApplication.class)
public class ScalarServiceTest {
  @Autowired
  private ScalarService scalarService;

  private static class ScalarDescriptionPrototype {
    private static ScalarDescription scalarDescription = new ScalarDescription();

    static {
      scalarDescription.setDataSource(new String[]{"dataflow.airuuid"});
      scalarDescription.setJobType("scalar");
      scalarDescription.setLimit(-1);
      scalarDescription.setTarget("AQI");
      scalarDescription.setAggregation("count");
      ScalarInputJson scalarInputJson = new ScalarInputJson();
      scalarInputJson.setScalarDescription(scalarDescription);
      scalarInputJson.setJob("operator-scalar");
      scalarInputJson.setOperatorType("scalar");
      scalarInputJson.setRequestId("xxxx");
      scalarInputJson.setWorkspaceId("xxxx");
    }

    static public ScalarDescription copy(String target, String aggregation) {
      ScalarDescription desc = new ScalarDescription();
      BeanUtils.copyProperties(scalarDescription, desc);
      desc.setTarget(target);
      desc.setAggregation(aggregation);
      return desc;
    }
  }

  @ParameterizedTest
  @CsvSource(value = {
      "AQI, average",
      "AQI, min",
      "AQI, max",
      "AQI, count",
      "AQI, distinct count",
      "AQI, sum",
      "AQI, standard dev",
})
  public void testNotNull(String target, String aggregation) {
    ScalarDescription scalarDescription = ScalarDescriptionPrototype.copy(target, aggregation);
    List<Map<String, Object>> value = scalarService.execute(scalarDescription);
    System.out.println(MessageFormat.format("[Scalar Description]: {0}", scalarDescription));
    System.out.println(value);
    Assertions.assertNotNull(value);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "AQI,  ''"
  })
  public void testNull(String target, String aggregation) {
    ScalarDescription scalarDescription = ScalarDescriptionPrototype.copy(target, aggregation);
    List<Map<String, Object>> value = scalarService.execute(scalarDescription);
    System.out.println(MessageFormat.format("[Scalar Description]: {0}", scalarDescription));
    Assertions.assertNull(value);
  }

  // Test a sub-query statement as datasource.
  @Test
  public void testLinkageDS() {
    ScalarDescription scalarDescription = ScalarDescriptionPrototype.copy("AQI", "count");
    scalarDescription.setDataSource(new String[] {"(SELECT * FROM dataflow.airuuid WHERE AQI <= 100 AND SO2 >= 60)"});
    List<Map<String, Object>> value = scalarService.execute(scalarDescription);
    System.out.println(MessageFormat.format("[Scalar Description]: {0}", scalarDescription));
    Assertions.assertNotNull(value);
  }
}
