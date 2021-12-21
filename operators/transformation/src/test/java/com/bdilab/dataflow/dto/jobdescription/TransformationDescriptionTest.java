package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.IndependentVariable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zunjing Chen
 * @date 2021-12-20
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TransformationDescriptionTest {

  @Test
  public void independentVariableEnumerationTest() {
    String json = "{\n"
        + "    \"independentVariables\": [\n"
        + "        {\n"
        + "            \"columnName\": \"$aa$\",\n"
        + "            \"defaultValue\": \"10\",\n"
        + "            \"type\": \"enumeration\",\n"
        + "            \"dataType\": \"string\",\n"
        + "            \"expression\": \"$aa$\",\n"
        + "            \"possibleValues\": [\n"
        + "                \"sad\",\"happy\"\n"
        + "            ],\n"
        + "            \"lowerBound\": \"0\",\n"
        + "            \"upperBound\": \"100\",\n"
        + "            \"ofValues\": \"10\"\n"
        + "        }\n"
        + "    ]\n"
        + "}";
    TransformationDescription decs = JSONObject.parseObject(json, TransformationDescription.class);
    for (IndependentVariable independentVariable : decs.getIndependentVariables()) {
      System.out.println(independentVariable.possibleValues());
    }

  }
  @Test
  public void independentVariableRangeTest() {
    String json = "{\n"
        + "    \"independentVariables\": [\n"
        + "        {\n"
        + "            \"columnName\": \"$aa$\",\n"
        + "            \"defaultValue\": \"10\",\n"
        + "            \"type\": \"range\",\n"
        + "            \"dataType\": \"float\",\n"
        + "            \"expression\": \"$aa$\",\n"
        + "            \"possibleValues\": [\n"
        + "                \"sad\",\"happy\"\n"
        + "            ],\n"
        + "            \"lowerBound\": \"0\",\n"
        + "            \"upperBound\": \"100\",\n"
        + "            \"ofValues\": \"10.0\"\n"
        + "        }\n"
        + "    ]\n"
        + "}";
    TransformationDescription decs = JSONObject.parseObject(json, TransformationDescription.class);
    for (IndependentVariable independentVariable : decs.getIndependentVariables()) {
      System.out.println(independentVariable.possibleValues());
    }
  }
}
