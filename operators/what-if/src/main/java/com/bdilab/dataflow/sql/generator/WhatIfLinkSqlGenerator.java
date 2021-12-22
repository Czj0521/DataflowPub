package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.dto.pojo.Expression;
import com.bdilab.dataflow.dto.pojo.independentvariable.BaseIndependentVariable;
import java.util.regex.Matcher;
import lombok.extern.slf4j.Slf4j;

/**
 * What-If: sql generator for link job with transformation.
 *
 * @author: wh
 * @create: 2021-12-20
 */
@Slf4j
public class WhatIfLinkSqlGenerator extends WhatIfBaseSqlGenerator {

  private final Expression baseVariable;

  /**
   * Constructor.
   *
   * @param whatIfDescription whatIf description
   * @param baseVariable base variable
   */
  public WhatIfLinkSqlGenerator(WhatIfDescription whatIfDescription, Expression baseVariable) {
    super(whatIfDescription);
    for (BaseIndependentVariable independentVariable : baseVariable.getIndependentVariables()) {
      String independentVariableName = independentVariable.getIndependentVariableName();
      String newName = "`" + independentVariableName + "`";
      baseVariable.getDependentVariables().forEach((depVar) -> {
        String temp = depVar.getExpression()
            .replaceAll(Matcher.quoteReplacement(independentVariableName),
                Matcher.quoteReplacement(newName));
        depVar.setExpression(temp);
      });
      independentVariable.setIndependentVariableName(newName);
    }
    //todo 目前版本 expression 会出现[Age] + $a$的问题，需要改为[Age] + `$a$`
    this.baseVariable = baseVariable;
  }

  @Override
  public String generate() {
    return with() + " " + project() + " " + datasource(0) + " " + group();
  }

  /**
   * 'WITH' syntax in sql.
   *
   * @return WITH SQL
   */
  public String with() {
    return "WITH " + baseVariable.combineIndependentVarWithAs()
        + ","
        + baseVariable.combineDependentVarWithAs();
  }

  @Override
  public String project() {
    return "SELECT " + baseVariable.independentVarToString() + "," + projectPostfix();
  }

  @Override
  public String datasource(int slotIndex) {
    return super.datasource(slotIndex);
  }

  @Override
  public String group() {
    return "GROUP BY " + baseVariable.independentVarToString();
  }

}
