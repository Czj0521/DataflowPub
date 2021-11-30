package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * description:
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
