package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.utils.SqlParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WhatIfBaseSqlGenerator extends SqlGeneratorBase {
  protected WhatIfDescription whatIfDescription;

  public WhatIfBaseSqlGenerator(WhatIfDescription whatIfDescription) {
    super(whatIfDescription);
    this.whatIfDescription = whatIfDescription;
  }

  @Override
  public String project() {
    String[] collectors = whatIfDescription.getCollectors();
    if (collectors == null || collectors.length == 0) {
      throw new RuntimeException("What-If is not ready !");
    }
    return "SELECT " + SqlParseUtils.combineWithSeparator(collectors, ",");
  }

  @Override
  public String generate() {
    return project() + super.datasource(0);
  }
}
