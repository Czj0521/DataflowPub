package com.bdilab.dataflow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * statistical test operator description.

 * @author YuShaochao
 * @create 2021-12-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalTestDescription {
  String test;
  String control;
  String type;
  private String[] dataSource;
}
