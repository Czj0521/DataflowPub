package com.bdilab.dataflow.dto.pojo.independentvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base Independent variable for What-If job .

 * @author: wh
 * @create: 2021-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIndependentVariable {
  protected String independentVariableName;

  /**
   * Get independent variable array.
   *
   * @return Array of variables.
   */
  public abstract String generateArray();

  /**
   * Transform independent variable to the format of sql with.
   *
   * @return Array of variables.
   */
  public String toWithAs() {
    return "arrayJoin(" + this.generateArray() + ") as " + this.independentVariableName;
  }
}
