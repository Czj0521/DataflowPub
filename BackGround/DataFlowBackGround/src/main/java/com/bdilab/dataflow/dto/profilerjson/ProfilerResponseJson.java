package com.bdilab.dataflow.dto.profilerjson;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
/**
 * @author wjh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerResponseJson {
    private String job;
    private String jobId;
    private Map<String,Object> responseJobInfo;
    private String workspaceId;
}
