package com.bdilab.dataflow.common.enums;

/**
 * Date Enum.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 */
public enum DateEnum {
  /**
   * Used for date type differentiation.
   */
  YEAR("year", 1),
  MONTH("month", 2),
  WEEK("week", 3),
  DAY("day", 4),
  HOUR("hour", 5),
  MINUTE("minute", 6),
  SECOND("second", 7);

  private final String dateFieldName;
  private final Integer index;

  DateEnum(String dateFieldName, Integer index) {
    this.dateFieldName = dateFieldName;
    this.index = index;
  }

  public String getDateFieldName() {
    return dateFieldName;
  }

  public Integer getIndex() {
    return index;
  }
}
