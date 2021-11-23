package com.bdilab.dataflow.utils.dag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DagNodeInputDto {
  private String nodeId;
  private String[] dataSources;
  private String nodeType;
  private Object nodeDescription;
}
