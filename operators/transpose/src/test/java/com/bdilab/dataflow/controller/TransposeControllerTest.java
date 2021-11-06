package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test TransposeController.
 *
 * @author: Zunjing Chen, Pan Liu
 * @create: 2021-11-2
 * @description:
 **/
@SpringBootTest
public class TransposeControllerTest {
  @Autowired
  TransposeController transposeController;
  /**
   *  initialize transposeDescription.
   */
  /*@Before
  public void init() {
    transposeDescription = new TransposeDescription("transpose", "promotion_csv", 2000);
  }*/


  /*@Test
  public void TransposeTest() {
    String profilerJson = "{\n"
        + "    \"column\": \"LeaseLength\",\n"
        + "    \"columnIsNumeric\": true,\n"
        + "    \"groupBy\": [\"PurchasePrice\"],\n"
        + "    \"attributeWithAggregationMap\": {\"LeasePrice\": \"avg\"},\n"
        + "    \"topTransposedValuesNum\": 20,\n"
        + "    \"dataSource\": \"dataflow.promotion_csv\",\n"
        + "    \"jobType\": \"transpose\",\n"
        + "    \"limit\": 2000,\n"
        + "}";
    //transposeDescription = TransposeDescription.generateFromJson(profilerJson);
    System.out.println(transposeController.transpose(transposeDescription));
  }*/

  @ParameterizedTest
  @MethodSource("aggregateProvider")
  public void testTranspose(HashMap<String,Object> map) {
    TransposeDescription transposeDescription = new TransposeDescription("transpose", "promotion_csv", 2000);
    transposeDescription.setColumn((String) map.get("column"));
    transposeDescription.setColumnIsNumeric((Boolean) map.get("columnIsNumeric"));
    transposeDescription.setGroupBy((String[]) map.get("groupBy"));
    transposeDescription.setAttributeWithAggregationMap((Map<String, String>) map.get("attributeWithAggregationMap"));
    transposeDescription.setTopTransposedValuesNum((Integer) map.get("topTransposedValuesNum"));
    System.out.println(transposeController.transpose(transposeDescription));
  }

  private static List<Arguments> aggregateProvider() {
    return Arrays.<Arguments>asList(
        // 1、column is numeric.
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","LeaseLength");
                put("columnIsNumeric",true);
                put("groupBy",new String[]{"PurchasePrice"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "avg");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 2、column is not numeric.
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "avg");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 3、横轴值的最大数量
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "avg");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",2);
              }
            }
        ),
        // 4、计算方式 -- count
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "count");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 5、计算方式 -- distinct count
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "distinct count");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 6、计算方式 -- sum
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "sum");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 7、计算方式 -- min
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "min");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 8、计算方式 -- max
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "max");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        ),
        // 9、纵轴 -- 多个
        Arguments.arguments(
            new HashMap<String,Object>(){
              {
                put("column","Married");
                put("columnIsNumeric",false);
                put("groupBy",new String[]{"EducationLevel", "LeaseLength"});
                Map<String, String> attributeWithAggregationMap = new HashMap<>();
                attributeWithAggregationMap.put("LeasePrice", "avg");
                put("attributeWithAggregationMap", attributeWithAggregationMap);
                put("topTransposedValuesNum",20);
              }
            }
        )
    );
  }
}
