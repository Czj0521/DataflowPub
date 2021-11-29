package com.bdilab.dataflow.utils.dag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DagNodeInputDto for node constructor.
 *
 * @author wh
 * @date 2021/11/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DagNodeInputDto {
  private String nodeId;
  private String nodeType;
  private Object nodeDescription;
}
