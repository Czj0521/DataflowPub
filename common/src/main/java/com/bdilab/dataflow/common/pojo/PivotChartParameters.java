package com.bdilab.dataflow.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PivotChart Parameters.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartParameters {
  private String mark;
  //todo 变量名改了，注意修改之后代码
  private PivotChartParameterInfo txAxis;
  //todo 变量名改了，注意修改之后代码
  private PivotChartParameterInfo tyAxis;
  private PivotChartParameterInfo color;
  private PivotChartParameterInfo size;
  private PivotChartParameterInfo row;
  private PivotChartParameterInfo column;
}
