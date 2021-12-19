package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WhatIfLinkSqlGenerator extends WhatIfBaseSqlGenerator {

  public final static Pattern COLLECTOR_PATTER = Pattern.compile("\\`.*\\`");

  private Expression baseVariable;

  public WhatIfLinkSqlGenerator(WhatIfDescription whatIfDescription, Expression baseVariable) {
    super(whatIfDescription);
    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      String independentVariableName = independentVariable.getIndependentVariableName();
      Matcher matcher = COLLECTOR_PATTER.matcher(independentVariableName);
      if (matcher.find()) {
        System.out.println(matcher.group(0));
        System.out.println(matcher.group(1));
      } else {
        System.out.println(independentVariableName);
        String newName = MessageFormat.format("`{0}`", independentVariableName);
        baseVariable.getDependentVariables().forEach((depVar) -> {
          String temp = depVar.getExpression().replaceAll(independentVariableName, newName);
          depVar.setExpression(temp);
        });
        independentVariable.setIndependentVariableName(newName);
//        throw new RuntimeException("Error !");
      }
    }
    this.baseVariable = baseVariable;
    System.out.println(baseVariable);
  }

  @Override
  public String project() {
    StringBuilder projectSql = new StringBuilder();

    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      projectSql.append(MessageFormat.format("arrayJoin({}) as `{}`",
          independentVariable.generateArray(),
          independentVariable.getIndependentVariableName()))
          .append(",");
    }
//    projectSql.append("SELECT ").append();
    return projectSql.toString();
  }

  @Override
  public String generate() {
    System.out.println(project());
    System.out.println("WhatIfLinkSqlGenerator");
    return super.datasource(0);
  }
}
