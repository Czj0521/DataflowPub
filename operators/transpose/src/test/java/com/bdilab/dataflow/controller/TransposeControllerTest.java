package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test TransposeController.
 *
 * @author: Zunjing Chen, Pan Liu
 * @create: 2021-11-2
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransposeControllerTest {
  @Autowired
  TransposeController transposeController;

  TransposeDescription transposeDescription;

  /**
   *  initialize transposeDescription.
   */
  @Before
  public void init() {
    transposeDescription = new TransposeDescription("transpose", "promotion_csv", 2000);
  }

  @Test
  public void testTranspose() {
    String profilerJson = "{\n"
        + "    \"dataSource\": \"dataflow.promotion_csv\",\n"
        + "    \"column\": \"Married\",\n"
        + "    \"columnIsNumeric\": \"false\",\n"
        + "    \"groupBy\": [\"EducationLevel\"],\n"
        + "    \"attributeWithAggregationMap\": {\"Age\": \"avg\"},\n"
        + "    \"topTransposedValuesNum\": 20,\n"
        + "    \"jobType\": \"transpose\",\n"
        + "    \"limit\": 2000,\n"
        + "}";
    transposeDescription = JSONObject.parseObject(profilerJson, TransposeDescription.class);
    System.out.println(transposeController.transpose(transposeDescription));
  }
}
