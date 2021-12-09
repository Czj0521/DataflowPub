package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Expression dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class Expression{

  @NotEmpty
  @ApiModelProperty(value = "新的列名", required = true)
  private String newColumnName;
  @NotEmpty
  @ApiModelProperty(value = "新列表达式", required = true)
  private String expression;

  @Override
  public String toString() {
    return expression + " AS " + newColumnName;
  }
}
