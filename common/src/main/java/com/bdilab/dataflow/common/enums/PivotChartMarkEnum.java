package com.bdilab.dataflow.common.enums;

/**
 * Enumerated classes for PivotChart dashboard types.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 */
public enum PivotChartMarkEnum {
  /**
   * Types of charts - bar chart, line chart, area chart, point chart, tick plot.
   */
  BAR("bar"),
  LINE("line"),
  AREA("area"),
  POINT("point"),
  TICK("tick");
  private final String markType;

  PivotChartMarkEnum(String markType) {
    this.markType = markType;
  }

  public String getMarkType() {
    return markType;
  }
}
