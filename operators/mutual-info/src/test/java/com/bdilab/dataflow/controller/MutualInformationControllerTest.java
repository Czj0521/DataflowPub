package com.bdilab.dataflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdilab.dataflow.MiTestApplication;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


/**
 * @author: Guo Yongqiang
 * @date: 2021/12/25 0:23
 * @version:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MiTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MutualInformationControllerTest {

  @Autowired
  private MutualInformationController mutualInformationController;

  @Test
  @SuppressWarnings("unchecked")
  public void accessiableFeatures() {
    String[] dataSources = new String[] {
        "operator_mi.car_sales",
        "operator_mi.promotion",
        "operator_mi.pointwise_mutual_information",
    };

    ResponseEntity<List<Map<String, Object>>> resp = mutualInformationController.accessibleFeatures(dataSources);

    List<Map<String, Object>> respBody = resp.getBody();
    JSONArray expected = JSON.parseArray("[\n" +
        "  {\n" +
        "    \"operator_mi.car_sales\": [\n" +
        "      \"Model\",\n" +
        "      \"Type\",\n" +
        "      \"ExteriorColor\",\n" +
        "      \"InteriorColor\",\n" +
        "      \"CarYear\",\n" +
        "      \"Used\",\n" +
        "      \"Lease\",\n" +
        "      \"DealerId\"\n" +
        "    ],\n" +
        "    \"operator_mi.promotion\": [\n" +
        "      \"Married\",\n" +
        "      \"EducationalLevel\",\n" +
        "      \"Employed\",\n" +
        "      \"CreditScore\",\n" +
        "      \"LeaseLength\",\n" +
        "      \"PurchasePrice\",\n" +
        "      \"Incentive\",\n" +
        "      \"Accepted\",\n" +
        "      \"DealerId\",\n" +
        "      \"AppliedOnline\",\n" +
        "      \"AppliedAtDealership\"\n" +
        "    ],\n" +
        "    \"operator_mi.pointwise_mutual_information\": [\n" +
        "      \"x\",\n" +
        "      \"y\"\n" +
        "    ]\n" +
        "  }\n" +
        "]"
    );
    Assertions.assertNotNull(respBody);
    for (String ds: dataSources) {
      List<String> l = expected.getJSONObject(0).getJSONArray(ds).toJavaList(String.class);
      Assertions.assertEquals(l, respBody.get(0).get(ds));
    }
  }
}
