package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;
import com.bdilab.dataflow.utils.SqlParseUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.text.StringSubstitutor;

/**
 * generate SQL statement of transpose operator.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
public class TransposeSqlGenerator extends SqlGeneratorBase implements LinkSqlGenerator {

  private TransposeDescription transposeDescription;
  private List<String> columnValues;
  private static final String GROUP_BY = " GROUP BY ";
  private static final String GROUP_FUNCTION = "groupFunction";
  private static final String ATTRIBUTE = "attribute";
  private static final String COLUMN = "column";
  private static final String COLUMN_NAME = "columnName";
  private static final String COLUMN_VALUE = "columnValue";
  private static final String STRING_TEMPLATE = "${groupFunction}(${attribute}, "
      + "${column} = '${columnValue}')  AS `${columnName}`";
  private static final String NUMERIC_TEMPLATE = "${groupFunction}(${attribute},"
      + "${column} = ${columnValue})  AS `${columnName}`";

  /**
   * TransposeSqlGenerator's constructor.
   *
   * @param transposeDescription transpose's dto
   * @param columnValues         column value list
   */
  public TransposeSqlGenerator(@Valid TransposeDescription transposeDescription,
      List<String> columnValues) {
    super(transposeDescription);
    this.transposeDescription = transposeDescription;
    this.columnValues =
        columnValues.size() <= transposeDescription.getTopTransposedValuesNum() ? columnValues
            : columnValues.subList(0, transposeDescription.getTopTransposedValuesNum());
  }

  /**
   * generate sql like select ** ,** means columns.
   *
   * @return sql
   */
  @Override
  public String project() {
    StringBuilder stringBuilder = new StringBuilder("SELECT ");
    appendGroupColumns(stringBuilder);
    appendColumns(stringBuilder);
    return stringBuilder.toString();
  }


  private void appendColumns(StringBuilder stringBuilder) {
    String template = transposeDescription.isColumnIsNumeric() ? NUMERIC_TEMPLATE : STRING_TEMPLATE;
    Map<String, String> attributes = transposeDescription.getAttributeWithAggregationMap();
    for (String columnValue : columnValues) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        Map<String, String> valueMap = new HashMap<>();
        if (entry.getValue().equals("distinct count")) {
          valueMap.put(GROUP_FUNCTION, "countIf");
          valueMap.put(ATTRIBUTE, "distinct " + entry.getKey());
        } else {
          valueMap.put(GROUP_FUNCTION, entry.getValue() + "If");
          valueMap.put(ATTRIBUTE, entry.getKey());
        }
        valueMap.put(COLUMN, transposeDescription.getColumn());
        // columnName = "100KG_avg_age" 180KG means weight
        valueMap.put(COLUMN_NAME, columnValue + "_" + entry.getKey() + "_" + entry.getValue());
        valueMap.put(COLUMN_VALUE, columnValue);
        StringSubstitutor sub = new StringSubstitutor(valueMap);
        stringBuilder.append(sub.replace(template)).append(", ");
      }
    }
    // delete ,
    stringBuilder.deleteCharAt(stringBuilder.length() - 2);
  }

  /**
   * append group column names.
   */
  private void appendGroupColumns(StringBuilder stringBuilder) {
    String groupColumn = SqlParseUtils.combineWithSeparator(transposeDescription.getGroupBy(), ",");
    stringBuilder.append(groupColumn).append(", ");
  }


  @Override
  public String group() {
    return GROUP_BY + SqlParseUtils.combineWithSeparator(transposeDescription.getGroupBy(), ",");

  }


  @Override
  public String generate() {
    return project() + super.datasource(0) + group() + super.limit();
  }

  @Override
  public String generateDataSourceSql() {
    return project() + super.datasource(0) + group();
  }
}
