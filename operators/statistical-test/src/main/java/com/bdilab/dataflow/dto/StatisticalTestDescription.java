package com.bdilab.dataflow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalTestDescription {
  String test;
  String control;
  String type;
  private String[] dataSource;
}
