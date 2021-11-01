package com.bdilab.dataflow.common.pojo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PivotChart Axis Calibration pojo.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartAxisCalibration<T> {
  private T min;
  private T max;
  /**
   * numeric: Ticks represents the length of a scale on the coordinate axis;
   * date: Ticks represent an interval of years or months or days or hours or minutes.
   */
  private T step;
  /**
   * numeric: Step represents the number of scale (interval) of the coordinate axis;
   * date: Step represents the number of ticks in an interval.
   */
  private Integer ticks;
  private String type;
  private List<T> calibration;

  /**
   * The constructor.
   */
  public PivotChartAxisCalibration(T min, T max, T step, Integer ticks) {
    this.min = min;
    this.max = max;
    this.step = step;
    this.ticks = ticks;
  }
}
