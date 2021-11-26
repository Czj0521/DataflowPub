package com.bdilab.dataflow.controller;

import com.bdilab.dataflow.ScalarTestApplication;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.dto.jobinputjson.ScalarInputJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 16:42
 * @version:
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScalarTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ScalarControllerTest {

  @Autowired
  private ScalarController scalarController;

  @Before
  public void init() {

  }


  @Test
  public void testController() {
    ScalarDescription scalarDescription = new ScalarDescription();
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

    System.out.println(MessageFormat.format("[Request body]: {0}", scalarInputJson));
    System.out.println(scalarController.scalar(scalarInputJson));
  }
}
