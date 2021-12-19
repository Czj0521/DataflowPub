package com.bdilab.dataflow.dto.pojo;

import com.bdilab.dataflow.dto.pojo.independvariable.BaseVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expression {
  private String expression;
  private List<BaseVariable> independentVariables;
  private String dependentVariable;
}
