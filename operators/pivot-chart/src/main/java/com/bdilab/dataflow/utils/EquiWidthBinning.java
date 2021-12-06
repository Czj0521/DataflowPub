package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author : [zhangpeiliang]
 * @description : [等宽分箱]
 */
public class EquiWidthBinning {

    /**
     * 分箱的最小值
     */
    private static double min;

    /**
     * 分箱的箱子宽度
     */
    private static double step;

    /**
     * 分箱范围
     */
    private static int range;

    /**
     * 是否包含0
     */
    private static boolean include_zero;

    public EquiWidthBinning(Long maxValue, Long minValue, boolean include_zero) {
        EquiWidthBinning.include_zero = include_zero;

        double min = Double.parseDouble(String.valueOf(minValue));
        double max = Double.parseDouble(String.valueOf(maxValue));
        int numTicks = 20;
        if ((max - min) < numTicks) {
            step = 1;
            range = (int) (max - min + 1);
            EquiWidthBinning.min = min;
        } else {
            PivotChartAxisCalibration<Double> adaptiveNumericAxisCalibration = AdaptiveAxisCalibrationUtils.getAdaptiveNumericAxisCalibration(min, max, numTicks);
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
     * 分箱集合
     */
    public Set<Object> binningSet() {
        List<Number> rangeList = new ArrayList<>();
        if (include_zero) {
            rangeList.add(0.0);
        }
        for (int i = 0; i <= range; i++) {
            rangeList.add(min + i * step);
        }
        return new TreeSet<>(rangeList);
    }
}
