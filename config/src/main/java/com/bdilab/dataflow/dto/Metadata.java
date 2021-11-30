package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * description:
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
  private String dataSource;
  private Map<String, String> metadata;
}
