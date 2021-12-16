package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.WhatIfDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class WhatIfLinkSqlGenerator extends WhatIfBaseSqlGenerator {

  public WhatIfLinkSqlGenerator(WhatIfDescription whatIfDescription) {
    super(whatIfDescription);
  }

  @Override
  public String generate() {
    System.out.println("WhatIfLinkSqlGenerator");
    return super.datasource(0);
  }
}
