package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;

import java.text.MessageFormat;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 16:09
 * @version:
 */
public class ScalarSqlGenerator extends SqlGeneratorBase implements LinkSqlGenerator {
  private String target;
  private String aggregation;

  public ScalarSqlGenerator(ScalarDescription scalarDescription) {
    super(scalarDescription);
    target = scalarDescription.getTarget();
    aggregation = scalarDescription.getAggregation();
  }

  @Override
  public String generate() {
    return generateDataSourceSql();
  }

  @Override
  public String generateDataSourceSql() {
    return MessageFormat.format("SELECT {0}(`{1}`) AS scalar", aggregation, target) + datasource(0);
  }
}
