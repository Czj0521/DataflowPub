package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.independvariable.BaseVariable;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;


public class WhatIfLinkSqlGenerator extends WhatIfBaseSqlGenerator {

  private Expression baseVariable;

  public WhatIfLinkSqlGenerator(WhatIfDescription whatIfDescription, Expression baseVariable) {
    super(whatIfDescription);
    this.baseVariable = baseVariable;
  }

  @Override
  public String project() {
    StringBuilder projectSql = new StringBuilder();
    String dependentVariable = baseVariable.getDependentVariable();
    for (BaseVariable independentVariable : baseVariable.getIndependentVariables()) {
      projectSql.append(MessageFormat.format("arrayJoin({}) as `{}`",
          independentVariable.generateArray(),
          dependentVariable))
          .append(",");
    }
    projectSql.append("SELECT ").append(super.projectPostfix());
    return projectSql.toString();
  }

  @Override
  public String generate() {
    System.out.println(project());
    System.out.println("WhatIfLinkSqlGenerator");
    return super.datasource(0);
  }
}
