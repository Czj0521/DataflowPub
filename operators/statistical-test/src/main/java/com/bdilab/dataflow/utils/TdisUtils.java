package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.dto.TdisDescription;
import org.apache.commons.math3.distribution.TDistribution;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * t test utils.

 * @author YuShaochao
 * @create 2021-12-20
 */
@Component
public  class TdisUtils {

  /**
   * 拿到T值.
   */
  public double getT(TdisDescription tdisDescription) {
    double meanX1 = tdisDescription.getMeanX1();
    Long n1 = tdisDescription.getN1();
    double sampleVariance1 = tdisDescription.getSampleVariance1();

    double meanX2 = tdisDescription.getMeanX2();
    Long n2 = tdisDescription.getN2();
    double sampleVariance2 = tdisDescription.getSampleVariance2();

    double meanDiff = Math.abs(meanX1 - meanX2);
    double squS = 1.0 / (n1 - 1) * sampleVariance1 + 1.0 / (n2 - 1) * sampleVariance2;
    double t;
    t = meanDiff / Math.sqrt(squS);
    return  t;
  }

  /**
   * 得到P值.
   */
  public Map<String, Object> getP(TdisDescription tdisDescription) {

    Long n1 = tdisDescription.getN1();
    double spus1 = tdisDescription.getSampleVariance1() / (n1 - 1);

    Long n2 = tdisDescription.getN2();
    double spus2 = tdisDescription.getSampleVariance2() / (n2 - 1);

    double degree = Math.pow(spus1 + spus2, 2)
            / (Math.pow(spus1, 2) / (n1 - 1) + Math.pow(spus2, 2) / (n2 - 1));

    double t = getT(tdisDescription);
//    System.out.println("t="+t);
    TDistribution td = new TDistribution(degree);
    double cumulative = td.cumulativeProbability(t);
    double p = (1 - cumulative) * 2;

    Map<String, Object> map = new HashMap<>();
    map.put("stat",t);
    map.put("pValue",p);
    return map;
  }

}
