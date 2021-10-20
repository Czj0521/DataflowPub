package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/20
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartResponseJson {
    private Object job;
    private Object jobId;
    private ResponseJobInfor responseJobInfor;
    private Object workspaceId;
}
