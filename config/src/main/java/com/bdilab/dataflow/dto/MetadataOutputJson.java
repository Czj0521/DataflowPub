package com.bdilab.dataflow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description: Metadata.
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataOutputJson extends BaseOutputJson {
  private List<Metadata> inputs;

  /**
   * Construction method.
   *
   */
  public MetadataOutputJson(String jobStatus, String operatorId,
                            String workspaceId, String operatorName,
                            List<Metadata> inputMetadataSlots) {
    super.setJobStatus(jobStatus);
    super.setOperatorId(operatorId);
    super.setWorkspaceId(workspaceId);
    super.setOperatorName(operatorName);
    this.inputs = inputMetadataSlots;
  }
}
