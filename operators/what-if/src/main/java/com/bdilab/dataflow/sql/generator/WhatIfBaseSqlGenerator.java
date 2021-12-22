package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.utils.SqlParseUtils;

/**
 * What-If: sql generator for base job without transformation.
 *
 * @author: wh
 * @create: 2021-12-20
 */
public class WhatIfBaseSqlGenerator extends SqlGeneratorBase {
  protected WhatIfDescription whatIfDescription;

  public WhatIfBaseSqlGenerator(WhatIfDescription whatIfDescription) {
    super(whatIfDescription);
    this.whatIfDescription = whatIfDescription;
  }

  protected String projectPostfix() {
    String[] collectors = whatIfDescription.getCollectors();
    if (collectors == null || collectors.length == 0) {
      throw new RuntimeException("What-If is not ready !");
    }
    return SqlParseUtils.combineWithSeparator(collectors, ",");
  }

  @Override
  public String project() {
    return "SELECT " + projectPostfix();
  }

  @Override
  public String generate() {
    return project() + super.datasource(0);
  }
}
