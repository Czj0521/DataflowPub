package com.bdilab.dataflow.dto.profilerjson;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author wjh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerJson {
    private String job;
    private String jobType;
    private JobDescription jobDescription;
    private String jobId;
    private String workspaceId;
}
