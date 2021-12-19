package com.bdilab.dataflow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * T-test description.

 * @author YuShaochao
 * @create 2021-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TdisDescription {
  private double meanX1;
  private Integer n1;
  private double sampleVariance1;  //sampleVariance = std^2

  private double meanX2;
  private Integer n2;
  private double sampleVariance2;
}
