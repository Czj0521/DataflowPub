package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.dependentvariable.DependentVariable;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WhatIfLinkSqlGenerator extends WhatIfBaseSqlGenerator {

  private final Expression baseVariable;

  public WhatIfLinkSqlGenerator(WhatIfDescription whatIfDescription, Expression baseVariable) {
    super(whatIfDescription);
    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      String independentVariableName = independentVariable.getIndependentVariableName();
      String newName = MessageFormat.format("`{0}`", independentVariableName);
      baseVariable.getDependentVariables().forEach((depVar) -> {
        String temp = depVar.getExpression()
            .replaceAll(Matcher.quoteReplacement(independentVariableName), Matcher.quoteReplacement(newName));
        depVar.setExpression(temp);
      });
      independentVariable.setIndependentVariableName(newName);
    }
    this.baseVariable = baseVariable;
  }

  @Override
  public String generate() {
    StringBuilder sql = new StringBuilder();
    sql.append(with()).append(" ").append(project()).append(" ").append(datasource(0)).append(" ").append(group()).append(" ");
    log.debug("What If: {}", sql);
    return sql.toString();
  }

  public String with() {
    StringBuilder withSql = new StringBuilder("WITH ");
    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      withSql.append(
          MessageFormat.format("arrayJoin({0}) as {1}",
              independentVariable.generateArray(),
              independentVariable.getIndependentVariableName())
      ).append(",");
    }
    for (DependentVariable dependentVariable : baseVariable.getDependentVariables()) {
      withSql.append(
          MessageFormat.format("({0}) as {1}",
              dependentVariable.getExpression(),
              dependentVariable.getDependentVariableName())
          ).append(",");
    }
    return withSql.substring(0, withSql.length()-1);
  }

  @Override
  public String project() {
    StringBuilder projectSql = new StringBuilder("SELECT ");
    baseVariable.getIndependentVariables().forEach((inVar) -> {
      projectSql.append(inVar.getIndependentVariableName()).append(",");
    });
    projectSql.append(projectPostfix());
    return projectSql.toString();
  }

  @Override
  public String datasource(int slotIndex) {
    return super.datasource(slotIndex);
  }

  @Override
  public String group() {
    StringBuilder groupSql = new StringBuilder("GROUP BY ");
    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      groupSql.append(independentVariable.getIndependentVariableName()).append(",");
    }
    return groupSql.substring(0, groupSql.length()-1);
  }

}
