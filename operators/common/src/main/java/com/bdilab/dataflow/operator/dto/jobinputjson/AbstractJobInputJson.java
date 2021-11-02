package com.bdilab.dataflow.operator.dto.jobinputjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract JobInputJson for some operators.
 * Operators job can extends or not.
 *
 * @author: wh
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractJobInputJson {
  private String job;
  private String operatorType;
  private String requestId;
  private String workspaceId;
}
