package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartJson {
    private String job;
    private String jobType;
    private PivotChartDescription pivotChartDescription;
    private String jobId;
    private String workspaceId;
}
