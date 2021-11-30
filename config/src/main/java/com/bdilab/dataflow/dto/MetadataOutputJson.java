package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description: metadata.
 *
 * @author zhb
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataOutputJson extends BaseOutputJson {
  private List<Metadata> inputs;

  public MetadataOutputJson(String jobStatus, String operatorId, String workspaceId, List<Metadata> inputMetadataSlots) {
    super.setJobStatus(jobStatus);
    super.setOperatorId(operatorId);
    super.setWorkspaceId(workspaceId);
    this.inputs = inputMetadataSlots;
  }
}
