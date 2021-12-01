package com.bdilab.dataflow.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description: Metadata information.
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
