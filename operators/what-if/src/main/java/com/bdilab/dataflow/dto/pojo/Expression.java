package com.bdilab.dataflow.dto.pojo;

import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Expression from Transformation.
 *
 * @author: wh
 * @create: 2021-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expression {
  private List<BaseIndependentVariable> independentVariables = new ArrayList<>();
  private List<DependentVariable> dependentVariables = new ArrayList<>();
}
