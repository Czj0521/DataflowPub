package com.bdilab.dataflow.common.enums;

import com.bdilab.dataflow.common.consts.OperatorConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Filter Operator Enum.

 * @author: Zunjing Chen
 * @create: 2021-09-13
 */
public enum FilterOperatorEnum {
  /**
   * string.
   */
  STRING_EQUALS(
      "string",
      "equals",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + " = '" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  STRING_CONTAINS(
      "string",
      "contains",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + " like '%" + OperatorConstants.VALUE_MAGIC_NUMBER + "%'",
      false),
  STRING_CONTAINS_ANY(
      "string",
      "contains any",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + " like '%" + OperatorConstants.VALUE_MAGIC_NUMBER + "%'",
      false),
  STRING_CONTAINS_ALL(
      "string",
      "contains all",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + " like '%" + OperatorConstants.VALUE_MAGIC_NUMBER + "%'",
      false),
  STRING_STARTS_WITH(
      "string",
      "starts with",
      "startsWith(" + OperatorConstants.COLUMN_MAGIC_NUMBER
          + ",'" + OperatorConstants.VALUE_MAGIC_NUMBER + "')",
      true),
  STRING_ENDS_WITH(
      "string",
      "ends with",
      "endsWith(" + OperatorConstants.COLUMN_MAGIC_NUMBER
          + ",'" + OperatorConstants.VALUE_MAGIC_NUMBER + "')",
      true),
  STRING_NOT_CONTAINS(
      "string",
      "does not contains",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + " not like '%" + OperatorConstants.VALUE_MAGIC_NUMBER + "%'",
      false),

  /**
   * numeric.
   */
  NUMERIC_EQUALS(
      "numeric",
      "equals",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_NOT_EQUALS(
      "numeric",
      "does not equals",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "!=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_GREATER_THAN(
      "numeric",
      "greater than",
      OperatorConstants.COLUMN_MAGIC_NUMBER + ">" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_GREATER_THAN_OR_EQUAL_TO(
      "numeric",
      "greater than or equal to",
      OperatorConstants.COLUMN_MAGIC_NUMBER + ">=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_LESS_THAN(
      "numeric",
      "less than",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "<" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_LESS_THAN_OR_EQUAL_TO(
      "numeric",
      "less than or equal to",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "<=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  NUMERIC_RANGE(
      "numeric",
      "range",
      "",
      false),

  /**
   * date.
   */
  DATE_IS(
      "date",
      "is",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + "='" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  DATE_BEFORE(
      "date",
      "before",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + "<'" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  DATE_ON_OR_BEFORE(
      "date",
      "on or before",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + "<='" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  DATE_AFTER(
      "date",
      "after",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + ">'" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  DATE_ON_OR_AFTER(
      "date",
      "on or after",
      OperatorConstants.COLUMN_MAGIC_NUMBER
          + ">='" + OperatorConstants.VALUE_MAGIC_NUMBER + "'",
      true),
  DATE_RANGE(
      "date",
      "range",
      "",
      false),

  /**
   * boolean.
   */
  BOOLEAN_IS(
      "boolean",
      "is",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true),
  BOOLEAN_IS_NOT(
      "boolean",
      "is not",
      OperatorConstants.COLUMN_MAGIC_NUMBER + "!=" + OperatorConstants.VALUE_MAGIC_NUMBER,
      true);

  /**
   * Data Type: string numeric date boolean.
   */
  String dataType;
  /**
   * Operators name in Front-end.
   */
  String filterOperatorName;
  /**
   * Back-end SQL.
   */
  String sqlParam;
  /**
   * Whether back-end or clickhouse is supported.
   */
  boolean support;
  public static final Map<String, Map<String, String>> FILTER_OPERATORS = new HashMap<>();

  static {
    for (FilterOperatorEnum value : FilterOperatorEnum.values()) {
      Map<String, String> orDefault =
          FILTER_OPERATORS.getOrDefault(value.getDataType(), new HashMap<>());
      orDefault.put(value.getFilterOperatorName(), value.getSqlParam());
      FILTER_OPERATORS.put(value.getDataType(), orDefault);
    }
  }

  /**
   * getFilterOperator.

   * @param operatorName is not null.
   */
  public static FilterOperatorEnum getFilterOperator(String operatorName) {
    for (FilterOperatorEnum filterOperatorEnum : FilterOperatorEnum.values()) {
      if (filterOperatorEnum.filterOperatorName.equals(operatorName)) {
        return filterOperatorEnum;
      }
    }
    throw new NoSuchElementException();
  }

  FilterOperatorEnum(String dataType, String filterOperatorName, String sqlParam, boolean support) {
    this.dataType = dataType;
    this.filterOperatorName = filterOperatorName;
    this.sqlParam = sqlParam;
    this.support = support;
  }

  public String getFilterOperatorName() {
    return filterOperatorName;
  }

  public String getSqlParam() {
    return sqlParam;
  }

  public String getDataType() {
    return dataType;
  }
}
