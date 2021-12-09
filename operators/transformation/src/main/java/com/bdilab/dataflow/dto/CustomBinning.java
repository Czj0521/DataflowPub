package com.bdilab.dataflow.dto;

import java.util.List;

/**
 * CustomBinning dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
public class CustomBinning {

  private String newColumnName;
  private String defaultBin;
  // base on datatype to generate sql
  private boolean isNumeric;
  private List<Bin> bins;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("multiIf(");
    for (Bin bin : bins) {
      if (!isNumeric) {
        bin.setBinValue("'" + bin.getBinValue() + "'");
      }
      stringBuilder.append(bin.getFilter()).append(",").append(bin.getBinValue()).append(",");
    }
    stringBuilder.append(defaultBin).append(") AS ").append(newColumnName);
    return stringBuilder.toString();
  }
}
