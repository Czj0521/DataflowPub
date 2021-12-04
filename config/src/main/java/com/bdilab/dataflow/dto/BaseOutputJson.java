package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description: The base class of outputs.
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseOutputJson {
  private String jobStatus;
  private String operatorId;
  private String workspaceId;
  private String operatorName;
}
