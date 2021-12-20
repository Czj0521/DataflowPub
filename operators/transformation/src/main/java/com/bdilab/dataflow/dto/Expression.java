package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Expression dto for transformation.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class Expression {

  @NotEmpty
  @ApiModelProperty(value = "新的列名", required = true)
  private String newColumnName;
  @NotEmpty
  @ApiModelProperty(value = "新列表达式", required = true)
  private String expression;
  // expression is for whatIf，if true, transformation operator
  // does not need to take it into sql,but what if operator need.
  @NotEmpty
  @ApiModelProperty(value = "表达式是否为whatIf因变量", required = true)
  private boolean isWhatIf;

  @Override
  public String toString() {
    if (isWhatIf) {
      return "";
    }
    return expression + " AS " + newColumnName;
  }

  public String whatIfString() {
    return expression + " AS " + newColumnName;
  }
}
