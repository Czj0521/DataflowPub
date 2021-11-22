package com.bdilab.dataflow.utils.dag.dto;

import lombok.Data;

@Data
public class DagNodeInputDto {
  private String nodeId;
  private String[] dataSources;
  private String nodeType;
  private Object nodeDescription;
}
