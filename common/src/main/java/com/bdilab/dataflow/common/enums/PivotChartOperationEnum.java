package com.bdilab.dataflow.common.enums;

/**
 * PivotChart Operation Enum.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/19
 */
public enum PivotChartOperationEnum {
  /**
   * attribute：The column name.
   */
  ATTRIBUTE("attribute"),

  /**
   * binning：Binning Operator，including：Equi Wdith Binning and Natural Binning.
   */
  BINNING("binning"),

  /**
   * aggregation：Aggregation operations (count, sum, Distinct Count, min, Max, average).
   */
  AGGREGATION("aggregation"),

  /**
   * sort：sort.
   */
  SORT("sort");

  private final String operationName;

  PivotChartOperationEnum(String operationName) {
    this.operationName = operationName;
  }

  public String getOperationName() {
    return operationName;
  }
}
