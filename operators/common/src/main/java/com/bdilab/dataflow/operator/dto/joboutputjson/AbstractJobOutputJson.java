package com.bdilab.dataflow.operator.dto.joboutputjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract JobOnputJson for some operators.
 * Operators job can extends or not.

 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractJobOutputJson {
  private String jobStatus;
  private String requestId;
  private String workspaceId;
}
