package com.bdilab.dataflow.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * description: Results.
 *
 * @author zhb
 */

@Data
public class OutputData {
  private List<Map<String, Object>> data;
  private Map<String, String> metadata;
  private String chartType;

  public OutputData() {}

  public OutputData(List<Map<String, Object>> data, Map<String, String> metadata) {
    this.data = data;
    this.metadata = metadata;
  }

  public OutputData(List<Map<String, Object>> data, Map<String, String> metadata, String chartType) {
    this.data = data;
    this.metadata = metadata;
    this.chartType = chartType;
  }
}
