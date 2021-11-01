package com.bdilab.dataflow.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PivotChart Parameter Info.

 * @author wh
 * @version 1.0
 * @date 2021/09/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartParameterInfo {
  private String attribute;
  private String binning;
  private String aggregation;
  private String sort;
  private String type;
  private String dataType;
}
