package com.bdilab.dataflow.operator.dto.jobinputjson;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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
