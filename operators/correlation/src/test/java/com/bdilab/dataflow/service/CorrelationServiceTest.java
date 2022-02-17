package com.bdilab.dataflow.service;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.CorrelationDescription;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试三种相关系数.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CorrelationServiceTest {
  @Resource
  CorrelationService correlationService;

  CorrelationDescription correlationDescription;

  /**
   * 初始化 correlationDescription.
   */
  @Before
  public void init() {
    correlationDescription = new CorrelationDescription();
    correlationDescription.setDataSource(new String[]{"dataflow.promotion_csv"});
    String[] features = new String[]{"Age", "CreditScore", "LeaseLength", "LeasePrice",
        "RemainingLength", "PurchasePrice", "Incentive", "DealerId"};
    correlationDescription.setFeatures(features);
  }

  @Test
  public void testKendall() {
    correlationDescription.setMethod("kendall");

    DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
    dagNodeInputDto.setNodeDescription(JSONObject.toJSON(correlationDescription));

    DagNode dagNode = new DagNode(dagNodeInputDto);
    List<Map<String, Object>> results = correlationService.saveToClickHouse(dagNode, null);
    for (Map<String, Object> result : results) {
      System.out.println(result.get("attribute_0") + ", " + result.get("attribute_1") + ", " + result.get("correlation"));
    }
  }

  @Test
  public void testPearson() {
    correlationDescription.setMethod("pearson");

    DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
    dagNodeInputDto.setNodeDescription(JSONObject.toJSON(correlationDescription));

    DagNode dagNode = new DagNode(dagNodeInputDto);
    List<Map<String, Object>> results = correlationService.saveToClickHouse(dagNode, null);
    for (Map<String, Object> result : results) {
      System.out.println(result.get("attribute_0") + ", " + result.get("attribute_1") + ", " + result.get("correlation"));
    }
  }

  @Test
  public void spearmanTest() {
    correlationDescription.setMethod("spearman");

    DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
    dagNodeInputDto.setNodeDescription(JSONObject.toJSON(correlationDescription));

    DagNode dagNode = new DagNode(dagNodeInputDto);
    List<Map<String, Object>> results = correlationService.saveToClickHouse(dagNode, null);
    for (Map<String, Object> result : results) {
      System.out.println(result.get("attribute_0") + ", " + result.get("attribute_1") + ", " + result.get("correlation"));
    }
  }
}
