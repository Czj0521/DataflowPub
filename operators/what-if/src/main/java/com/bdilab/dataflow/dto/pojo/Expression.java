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
 * @create: 2021-12-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expression {
  private List<BaseIndependentVariable> independentVariables = new ArrayList<>();
  private List<DependentVariable> dependentVariables = new ArrayList<>();

  /**
   * Combine independent variables.
   */
  public String combineIndependentVarWithAs() {
    StringBuilder withSql = new StringBuilder();
    independentVariables.forEach((idv) -> {
      withSql.append(idv.toWithAs()).append(",");
    });
    return withSql.length() == 0 ? "" : withSql.substring(0, withSql.length() - 1);
  }

  /**
   * Combine dependent variables.
   */
  public String combineDependentVarWithAs() {
    StringBuilder withSql = new StringBuilder();
    dependentVariables.forEach((dv) -> {
      withSql.append(dv.toWithAs()).append(",");
    });
    return withSql.length() == 0 ? "" : withSql.substring(0, withSql.length() - 1);
  }

  /**
   * Transform independent variables to string.
   */
  public String independentVarToString() {
    StringBuilder sql = new StringBuilder();
    independentVariables.forEach((independentVar) -> {
      sql.append(independentVar.getIndependentVariableName()).append(",");
    });
    return sql.length() == 0 ? "" : sql.substring(0, sql.length() - 1);
  }
}
