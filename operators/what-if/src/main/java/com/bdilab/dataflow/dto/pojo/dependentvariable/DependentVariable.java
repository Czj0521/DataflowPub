package com.bdilab.dataflow.dto.pojo.dependentvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dependent variable for What-If job .

 * @author: wh
 * @create: 2021-12-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependentVariable {
  private String dependentVariableName;
  private String expression;

  /**
   * Transform independent variable to the format of sql with.
   *
   * @return Array of variables.
   */
  public String toWithAs() {
    return "(" + expression + ") as " + dependentVariableName;
  }
}
