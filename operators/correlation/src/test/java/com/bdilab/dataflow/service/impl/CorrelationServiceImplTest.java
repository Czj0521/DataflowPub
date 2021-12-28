package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.dto.jobDescription.CorrelationDescription;
import com.bdilab.dataflow.dto.joboutputjson.ResponseObj;
import com.bdilab.dataflow.service.CorrelationService;
import java.util.List;
import javax.annotation.Resource;
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
public class CorrelationServiceImplTest {
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
    correlationDescription.setLimit(2000);
    correlationDescription.setJobType("correlation");
    String[] features = new String[]{"Age", "CreditScore", "LeaseLength", "LeasePrice",
        "RemainingLength", "PurchasePrice", "Incentive", "DealerId"};
    correlationDescription.setFeatures(features);
  }

  @Test
  public void testKendall() {
    correlationDescription.setMethod("kendall");

    long before = System.currentTimeMillis();
    List<ResponseObj> responseObjList = correlationService.correlation(correlationDescription);
    long after = System.currentTimeMillis();
    log.info("Time spent :" + (after - before));

    assert responseObjList.get(0).getCorrelation().equals("0.00447");
  }

  @Test
  public void testPearson() {
    correlationDescription.setMethod("pearson");

    long before = System.currentTimeMillis();
    List<ResponseObj> responseObjList = correlationService.correlation(correlationDescription);
    long after = System.currentTimeMillis();
    log.info("Time spent :" + (after - before));

    assert responseObjList.get(0).getCorrelation().equals("-0.00345");
  }

  @Test
  public void spearmanTest() {
    correlationDescription.setMethod("spearman");

    long before = System.currentTimeMillis();
    List<ResponseObj> responseObjList = correlationService.correlation(correlationDescription);
    long after = System.currentTimeMillis();
    log.info("Time spent :" + (after - before));

    assert responseObjList.get(0).getCorrelation().equals("0.00668");
  }
}
