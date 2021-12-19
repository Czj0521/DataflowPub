package com.bdilab.dataflow.dto.pojo.independentvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIndependentVariable {
  private String independentVariableName;
  public abstract String generateArray();
}
