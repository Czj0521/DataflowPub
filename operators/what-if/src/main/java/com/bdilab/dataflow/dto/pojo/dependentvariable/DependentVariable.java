package com.bdilab.dataflow.dto.pojo.dependentvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dependent variable for What-If job .

 * @author: wh
 * @create: 2021-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependentVariable {
  private String dependentVariableName;
  private String expression;
}
