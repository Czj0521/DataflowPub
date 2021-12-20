package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.dto.IndependentVariableBase;
import com.bdilab.dataflow.dto.jobdescription.TransformationDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.EnumerationIndependentVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Transform pojo from TransformationDesc to Expression for what-if.
 *
 * @author Zunjing Chen
 * @date 2021-12-19
 */
public class WhatIfUtils {

  /**
   * WhatIf operator need last node(must be transformation operator) to calculate.
   */
  public static Expression transformtionToWhatIf(TransformationDescription transformationDesc) {
    if (transformationDesc == null) {
      return null;
    }
    List<BaseIndependentVariable> independentVariables = new ArrayList<>();
    List<DependentVariable> dependentVariables = new ArrayList<>();
    for (com.bdilab.dataflow.dto.Expression expression : transformationDesc.getExpressions()) {
      if (expression.isWhatIf()) {
        dependentVariables
            .add(new DependentVariable(expression.getNewColumnName(), expression.getExpression()));
      }
    }
    for (IndependentVariableBase variable : transformationDesc.getIndependentVariables()) {
      dependentVariables
          .add(new DependentVariable(variable.getColumnName(), variable.getExpression()));
      independentVariables.add(
          new EnumerationIndependentVariable(
              variable.getExpression().trim(),
              variable.getDefaultValue(),
              variable.possibleValues()
          )
      );
    }
    return new Expression(independentVariables, dependentVariables);
  }

}
