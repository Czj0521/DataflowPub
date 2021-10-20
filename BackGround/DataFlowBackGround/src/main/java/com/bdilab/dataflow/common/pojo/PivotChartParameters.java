package com.bdilab.dataflow.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartParameters {
    private String mark;
    private PivotChartParameterInfo xAxis;
    private PivotChartParameterInfo yAxis;
    private PivotChartParameterInfo color;
    private PivotChartParameterInfo size;
    private PivotChartParameterInfo row;
    private PivotChartParameterInfo column;
}
