package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.service.impl.TransposeServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


/**
 * Test TransposeSqlGenerator.
 *
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description:
 **/
public class TransposeSqlGeneratorTest {
  TransposeSqlGenerator transposeSqlGenerator;
  TransposeDescription transposeDescription;

  /**
   *  initialize transposeDescription.
   */
  @Before
  public void init() {
    transposeDescription = new TransposeDescription("transpose", "promotion_csv", 3000);
  }

  /*
  @Test
  public void appendGroupColumnsTest() throws Exception {
    StringBuilder stringBuilder = new StringBuilder();
    Whitebox.invokeMethod(transposeSqlGenerator, "appendGroupColumns", stringBuilder);
    Assert.assertEquals(stringBuilder.toString(), "name,");
  }

  @Test
  public void appendColumnsTest() throws Exception {
    StringBuilder stringBuilder = new StringBuilder();
    Whitebox.invokeMethod(transposeSqlGenerator, "appendColumns", stringBuilder);
    String re = stringBuilder.toString();
    // 列名正确性
    Assert.assertTrue(re.contains("W_avg_age"));
    Assert.assertTrue(re.contains("'W'"));
    Assert.assertTrue(re.contains("avgIf"));
  }

  @Test
  public void groupTest() {
    Assert.assertEquals(transposeSqlGenerator.group(), " GROUP BY name");
  }

  @Test
  public void limitTest() {
    Assert.assertEquals(transposeSqlGenerator.limit(), " LIMIT 3000");
  }
   */

  /**
   * Test one: column is Numeric.
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
   * Test two: attribute is not Numeric.
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
   * Test three: Longitudinal axis is not unique.
   */
  @Test
  public void LongaxisIsNotUniqueTest() {
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
   * Test four: Same Attribute.
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
