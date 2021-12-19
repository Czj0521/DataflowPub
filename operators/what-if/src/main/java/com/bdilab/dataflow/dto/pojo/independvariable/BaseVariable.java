package com.bdilab.dataflow.dto.pojo.independvariable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseVariable {
  private String variableName;
  public abstract String generateArray();
}
