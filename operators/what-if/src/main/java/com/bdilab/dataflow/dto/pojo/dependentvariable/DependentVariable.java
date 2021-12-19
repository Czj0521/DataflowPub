package com.bdilab.dataflow.dto.pojo.dependentvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependentVariable {
  private String dependentVariableName;
  private String expression;
}
