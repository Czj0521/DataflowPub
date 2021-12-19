package com.bdilab.dataflow.dto.pojo;

import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expression {
  private List<BaseIndependentVariable> independentVariables = new ArrayList<>();
  private List<DependentVariable> dependentVariables = new ArrayList<>();
}
