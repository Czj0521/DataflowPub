package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
import java.util.Calendar;
import java.util.Date;

/**
 * Adaptive Axis Calibration Utils.

 * @author wh
 * @date 2021/09/17。
 */
public class AdaptiveAxisCalibrationUtils {
    private static final int MIN_NUM_TICKS = 4;
    private static final int MIN_DATE_TICKS = 3;
    private static final long DATE_YEAR_TIME = 31536000000L;
    private static final long DATE_MONTH_TIME = 2592000000L;
    private static final long DATE_DAY_TIME = 24 * 60 * 60 * 1000;
    private static final long DATE_HOUR_TIME = 60 * 60 * 1000;
    private static final long DATE_MINUTE_TIME = 60 * 1000;
    private static final long DATE_SECOND_TIME = 1000;

    /**
     * Adaptive axis calibration for NUMERIC.

     * @param min min value
     * @param max max value
     * @param numTicks Step number
     * @return Axis Calibration
     */
    public static PivotChartAxisCalibration<Double> getAdaptiveNumericAxisCalibration(
            double min,
            double max,
            int numTicks) {
        if (numTicks < MIN_NUM_TICKS) {
            numTicks = MIN_NUM_TICKS;
        }
        double grossStep = (max - min) / numTicks;
        double step = Math.pow(10, Math.floor(Math.log10(grossStep)));
        if (grossStep < 2 * step) {
            step = step * 2;
        } else if (grossStep >= 2 * step && grossStep < 3 * step) {
            step = step * 3;
        } else if (grossStep >= 3 * step && grossStep < 4 * step) {
            step = step * 4;
        } else if (grossStep >= 4 * step && grossStep < 5 * step) {
            step = step * 5;
        } else {
            step = step * 10;
        }
        numTicks = (int) (Math.ceil(max / step) - Math.floor(min / step));
        min = Math.floor(min / step) * step;
        max = Math.ceil(max / step) * step;
        return new PivotChartAxisCalibration<>(min, max, step, numTicks);
    }

    /**
     * Adaptive axis calibration for DATE.

     * @param min min value
     * @param max max value
     * @param type value type
     * @return Axis Calibration
     */
    public static PivotChartAxisCalibration<Long> getAdaptiveDateAxisCalibration(
            Date min,
            Date max,
            String type) {
        long step;
        int numTicks = MIN_DATE_TICKS;
        long grossStep = max.getTime() - min.getTime();

        if (grossStep > (long) numTicks * DATE_YEAR_TIME) {
            step = Calendar.YEAR;
            numTicks = 1;
        } else if (grossStep > (long) DATE_YEAR_TIME) {
            step = Calendar.MONTH;
            numTicks = 3;
        } else if (grossStep > (long) numTicks * DATE_MONTH_TIME) {
            step = Calendar.MONTH;
            numTicks = 1;
        } else if (grossStep > DATE_MONTH_TIME) {
            step = Calendar.DAY_OF_WEEK;
            numTicks = 7;
        } else if (DataTypeEnum.DATE.getColumnDateType().equals(type)
                    || (grossStep > numTicks * DATE_DAY_TIME 
                && DataTypeEnum.DATETIME.getColumnDateType().equals(type))) {
            step = Calendar.DAY_OF_WEEK;
            numTicks = 1;
        } else if (grossStep > (long) DATE_DAY_TIME) {
            step = Calendar.HOUR;
            numTicks = 6;
        } else if (grossStep > (long) numTicks * DATE_HOUR_TIME) {
            step = Calendar.HOUR;
            numTicks = 1;
        } else if (grossStep > DATE_HOUR_TIME) {
            step = Calendar.MINUTE;
            numTicks = 15;
        } else if (grossStep > (long) numTicks * DATE_MINUTE_TIME) {
            step = Calendar.MINUTE;
            numTicks = 1;
        } else if (grossStep > DATE_MINUTE_TIME) {
            step = Calendar.SECOND;
            numTicks = 15;
        } else if (DataTypeEnum.DATETIME.getColumnDateType().equals(type)
                    || (grossStep > numTicks * DATE_SECOND_TIME
                        && DataTypeEnum.DATETIME64.getColumnDateType().equals(type))) {
            step = Calendar.SECOND;
            numTicks = 1;
        } else {
            step = Calendar.MILLISECOND;
            numTicks = 100;
        }
        return new PivotChartAxisCalibration<>(min.getTime(), max.getTime(), step, numTicks);
    }
}
