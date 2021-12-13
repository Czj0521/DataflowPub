package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.common.consts.OperatorConstants;
import com.bdilab.dataflow.common.enums.GroupOperatorEnum;
import com.bdilab.dataflow.dto.jobdescription.ScalarDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 16:09
 * @version:
 */
public class ScalarSqlGenerator extends SqlGeneratorBase implements LinkSqlGenerator {
  private String target;
  private String aggregation;

  private final static Map<String, String> AGG_MAP = new HashMap<>();

  static {
    for (GroupOperatorEnum group: GroupOperatorEnum.values()) {
      AGG_MAP.put(group.getOperatorName(), group.getSqlParam());
    }
  }

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

    String aggExpression = AGG_MAP.get(aggregation).replaceAll(OperatorConstants.COLUMN_MAGIC_NUMBER, target);
    return MessageFormat.format("SELECT {0} AS scalar", aggExpression) + datasource(0);
  }
}
