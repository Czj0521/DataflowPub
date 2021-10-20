package com.bdilab.dataflow.dto.pivotchartjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartDescription {
    private String dataSource;
    private String axisDimension;
    private String mark;
    private List<Operations> operations;
    private String output;
}
