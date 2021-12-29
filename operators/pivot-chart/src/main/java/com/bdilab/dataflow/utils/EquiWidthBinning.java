package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 等宽分箱.
 * @ author: [zhangpeiliang]
 */
public class EquiWidthBinning {

  /**
   * 分箱的最小值.
   */
  private static double min;

  /**
   * 分箱的箱子宽度.
   */
  private static double step;

  /**
   * 分箱范围.
   */
  private static int range;

  /**
   * 是否包含0.
   */
  private static boolean includeZero;

  /**
   * 等宽分箱构造函数.
   */
  public EquiWidthBinning(Double maxValue, Double minValue, boolean includeZero) {
    EquiWidthBinning.includeZero = includeZero;

    double min = minValue;
    double max = maxValue;
    int numTicks = 20;
    if ((max - min) > 1 && (max - min) < numTicks) {
      step = 1;
      range = (int) (max - min + 1);
      EquiWidthBinning.min = min;
    } else {
      PivotChartAxisCalibration<Double> adaptiveNumericAxisCalibration
              = AdaptiveAxisCalibrationUtils.getAdaptiveNumericAxisCalibration(min, max, numTicks);
      step = adaptiveNumericAxisCalibration.getStep();

      if (max == adaptiveNumericAxisCalibration.getMax()) {
        range = adaptiveNumericAxisCalibration.getTicks() + 1;
      } else {
        range = adaptiveNumericAxisCalibration.getTicks();
      }
      EquiWidthBinning.min = adaptiveNumericAxisCalibration.getMin();
    }
  }

  /**
   * 分箱集合.
   */
  public Set<Object> binningSet() {
    List<Double> rangeList = new ArrayList<>();
    if (includeZero) {
      rangeList.add(0.0);
    }
    for (int i = 0; i <= range; i++) {
      BigDecimal bigDecimal = new BigDecimal(min + i * step);
      double val = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
      rangeList.add(val);
    }
    return new TreeSet<>(rangeList);
  }

}
