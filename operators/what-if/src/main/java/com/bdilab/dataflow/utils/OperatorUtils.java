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
 * @author Zunjing Chen
 * @date 2021-12-19
 **/
public class OperatorUtils {

  /**
   * WhatIf operator need last node(must be transformation operator) to calculate.
   */
  public static Expression transformtionToWhatIf(TransformationDescription transformationDesc) {
    List<BaseIndependentVariable> independentVariables = new ArrayList<>();
    List<DependentVariable> dependentVariables = new ArrayList<>();
    for (com.bdilab.dataflow.dto.Expression expression : transformationDesc.getExpressions()) {
      if (expression.isWhatIf()) {
        dependentVariables
            .add(new DependentVariable(expression.getNewColumnName(), expression.getExpression()));
      }
    }
    for (IndependentVariableBase variable : transformationDesc.getIndependentVariables()) {
      independentVariables.add(
          new EnumerationIndependentVariable(variable.getColumnName(), variable.getDefaultValue(),
              variable.possibleValues()));
    }
    return new Expression(independentVariables, dependentVariables);
  }

}
