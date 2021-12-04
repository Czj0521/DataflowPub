package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * description: JobOutputJson for linkage.
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOutputJson extends BaseOutputJson {
  private OutputData outputs;

  /**
   * Construction method.
   */
  public JobOutputJson(String jobStatus, String operatorId,
                       String workspaceId, String operatorName, OutputData outputs) {
    super.setJobStatus(jobStatus);
    super.setOperatorId(operatorId);
    super.setWorkspaceId(workspaceId);
    super.setOperatorName(operatorName);
    this.outputs = outputs;
  }
}
