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

    public EquiWidthBinning(Set<Object> dataSet, boolean include_zero) {
        EquiWidthBinning.include_zero = include_zero;
        List<Object> doubleList = new ArrayList<>(dataSet);
        double min = Double.parseDouble(doubleList.get(0).toString());
        double max = Double.parseDouble(doubleList.get(doubleList.size() - 1).toString());
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

    /**
     * 获取最终要封装的值
     */
    public List<Object> values(List<?> dataList, Set<Object> binningSet) {
        List<Object> binningList = new ArrayList<>(binningSet);

        List<Object> values = new ArrayList<>();
        for (Object o : dataList) {
            List<Object> list = new ArrayList<>();
            Number value = (Number) o;
            int begin = binningList.indexOf(value);
            int end = begin + 1;
            list.add(binningList.get(begin));
            list.add(binningList.get(end));
            values.add(list);
        }
        return values;
    }
}
