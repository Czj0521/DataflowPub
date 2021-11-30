package com.bdilab.dataflow.dto;

import java.util.List;
import java.util.Map;

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
public class JobOutputJson {
  private String jobStatus;
  private String operatorId;
  private String workspaceId;
  private List<String> dataSource;
  private List<Map<String, Object>> outputs;
}
