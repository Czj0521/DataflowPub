package com.bdilab.dataflow.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description: Results.
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputData {
  private List<Map<String, Object>> data;
  private Map<String, String> metadata;
}
