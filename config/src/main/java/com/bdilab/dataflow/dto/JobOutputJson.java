package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * description: JobOutputJson for linkage.
 *
 * @author: zhb
 * @createTime: 2021/11/22 9:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOutputJson extends BaseOutputJson{
  private OutputData outputs;

  public JobOutputJson(String jobStatus, String operatorId, String workspaceId, OutputData outputs) {
    super.setJobStatus(jobStatus);
    super.setOperatorId(operatorId);
    super.setWorkspaceId(workspaceId);
    this.outputs = outputs;
  }
}
