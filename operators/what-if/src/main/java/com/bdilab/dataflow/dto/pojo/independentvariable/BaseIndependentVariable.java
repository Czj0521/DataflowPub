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
  private String independentVariableName;

  /**
   * Get independent variable array.
   *
   * @return Array of variables.
   */
  public abstract String generateArray();
}
