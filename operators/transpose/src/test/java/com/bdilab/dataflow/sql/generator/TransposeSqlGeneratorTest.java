package com.bdilab.dataflow.sql.generator;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.controller.TransposeController;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test TransposeSqlGenerator.
 *
 * @author: Zunjing Chen, Pan Liu
 * @create: 2021-10-25
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransposeSqlGeneratorTest {
  TransposeSqlGenerator transposeSqlGenerator;
  TransposeDescription transposeDescription;

  /**
   *  initialize transposeDescription.
   */
  @Before
  public void init() {
    transposeDescription = new TransposeDescription("transpose", "promotion_csv", 2000);
  }

  /**
   * Test 1: column is Numeric.
   */
  @Test
  public void columnIsNumericTest() {
    transposeDescription.setColumn("CreditScore");
    transposeDescription.setColumnIsNumeric(true);
    transposeDescription.setGroupBy(new String[]{"Married"});
    transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String, String>builder()
        .put("Age", "avg")
        .build());
    List<String> columnValues = Lists.newArrayList("102", "-32", "7", "60", "21");
    transposeSqlGenerator = new TransposeSqlGenerator(transposeDescription, columnValues);

    System.out.println(transposeSqlGenerator.generate());
  }

  /**
   * Test 2: attribute is not Numeric.
   */
  @Test
  public void attributeIsNotNumericTest() {
    transposeDescription.setColumn("Married");
    transposeDescription.setColumnIsNumeric(false);
    transposeDescription.setGroupBy(new String[]{"EducationLevel"});
    transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String, String>builder()
        .put("Married", "count")
        .build());
    List<String> columnValues = Lists.newArrayList("married", "single", "widow/widower");
    transposeSqlGenerator = new TransposeSqlGenerator(transposeDescription, columnValues);

    System.out.println(transposeSqlGenerator.generate());
  }

  /**
   * Test 3: Longitudinal axis is not unique.
   */
  @Test
  public void longaxisIsNotUniqueTest() {
    transposeDescription.setColumn("Married");
    transposeDescription.setColumnIsNumeric(false);
    transposeDescription.setGroupBy(new String[]{"Married", "EducationLevel"});
    transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String, String>builder()
        .put("Age", "avg")
        .build());
    List<String> columnValues = Lists.newArrayList("married", "single", "widow/widower");
    transposeSqlGenerator = new TransposeSqlGenerator(transposeDescription, columnValues);

    System.out.println(transposeSqlGenerator.generate());
  }

  /**
   * Test 4: Same Attribute.
   */
  @Test
  public void sameAttributeTest() {
    transposeDescription.setColumn("Married");
    transposeDescription.setColumnIsNumeric(false);
    transposeDescription.setGroupBy(new String[]{"Married"});
    transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String, String>builder()
        .put("Married", "count")
        .build());
    List<String> columnValues = Lists.newArrayList("married", "single", "widow/widower");
    transposeSqlGenerator = new TransposeSqlGenerator(transposeDescription, columnValues);

    System.out.println(transposeSqlGenerator.generate());
  }
}
