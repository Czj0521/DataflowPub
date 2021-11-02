package com.bdilab.dataflow.common.enums;

import com.bdilab.dataflow.common.consts.OperatorConstants;
import java.util.NoSuchElementException;

/**
 * Group Operator Enum.

 * @author: Zunjing Chen
 * @create: 2021-09-13
 */
public enum GroupOperatorEnum {
  /**
   * GroupOperator Enum.
   */
  NONE(false, "none", OperatorConstants.COLUMN_MAGIC_NUMBER),
  MAX(false, "max", "max(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  MIN(false, "min", "min(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  COUNT(false, "count", "count(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  DISTINCT_COUNT(false, "distinct count",
      "uniqExact(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  SUM(true, "sum", "sum(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  AVERAGE(true, "average", "avg(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")"),
  STANDARD_DEV(true, "standard dev", "stddevPop(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")");
  private final boolean onlyNumeric;
  private final String operatorName;
  private final String sqlParam;

  GroupOperatorEnum(boolean onlyNumeric, String operatorName, String sqlParam) {
    this.operatorName = operatorName;
    this.sqlParam = sqlParam;
    this.onlyNumeric = onlyNumeric;
  }

  /**
   * getGroupOperatorEnum.
   */
  public static GroupOperatorEnum getGroupOperatorEnum(String operatorName) {
    for (GroupOperatorEnum groupOperatorEnum : GroupOperatorEnum.values()) {
      if (groupOperatorEnum.operatorName.equals(operatorName)) {
        return groupOperatorEnum;
      }
    }
    throw new NoSuchElementException();
  }

  public boolean isOnlyNumeric() {
    return onlyNumeric;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public String getSqlParam() {
    return sqlParam;
  }
}
