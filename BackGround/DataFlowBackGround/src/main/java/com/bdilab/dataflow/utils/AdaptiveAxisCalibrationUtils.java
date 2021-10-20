package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 * pojo, 需要先生成实例再进行使用。
 */
public class AdaptiveAxisCalibrationUtils {
    private static final int MIN_NUM_TICKS = 4;
    private static final int MIN_DATE_TICKS = 3;
    private static final long DATE_YEAR_TIME = 31536000000L;
    private static final long DATE_MONTH_TIME = 2592000000L;
    private static final long DATE_DAY_TIME = 24*60*60*1000;
    private static final long DATE_HOUR_TIME = 60*60*1000;
    private static final long DATE_MINUTE_TIME = 60*1000;
    private static final long DATE_SECOND_TIME = 1000;

    public static PivotChartAxisCalibration<Double> getAdaptiveNumericAxisCalibration(double mMin, double mMax, int mNumTicks){
        double mStep;
        if(mNumTicks < MIN_NUM_TICKS){
            mNumTicks = MIN_NUM_TICKS;
        }
        double grossStep = (mMax - mMin) / mNumTicks;
        double step = Math.pow(10, Math.floor(Math.log10(grossStep)));
        if(grossStep < 2 * step){
            step = step * 2;
        } else if(grossStep >= 2 * step && grossStep < 3 * step ){
            step = step * 3;
        } else if(grossStep >= 3 * step && grossStep < 4 * step ){
            step = step * 4;
        } else if(grossStep >= 4 * step && grossStep < 5 * step ){
            step = step * 5;
        } else{
            step = step * 10;
        }
        mStep = step;
        mNumTicks = (int) (Math.ceil(mMax / step) - Math.floor(mMin / step));
        mMin = Math.floor(mMin / step) * step;
        mMax = Math.ceil(mMax / step) * step;
        return new PivotChartAxisCalibration<>(mMin, mMax, mStep, mNumTicks);
    }

    public static PivotChartAxisCalibration<Long> getAdaptiveDateAxisCalibration(Date mMin, Date mMax, String type){
        long mStep;
        int mNumTicks = MIN_DATE_TICKS;
        long grossStep = mMax.getTime() - mMin.getTime();

        if (grossStep > (long) mNumTicks*DATE_YEAR_TIME){
            mStep = Calendar.YEAR;
            mNumTicks = 1;
        } else if (grossStep > (long) DATE_YEAR_TIME){
            mStep = Calendar.MONTH;
            mNumTicks = 3;
        } else if (grossStep > (long) mNumTicks*DATE_MONTH_TIME){
            mStep = Calendar.MONTH;
            mNumTicks = 1;
        } else if (grossStep > DATE_MONTH_TIME){
            mStep = Calendar.DAY_OF_WEEK;
            mNumTicks = 7;
        } else if (DataTypeEnum.DATE.getColumnDateType().equals(type)
                    || (grossStep > mNumTicks*DATE_DAY_TIME && DataTypeEnum.DATETIME.getColumnDateType().equals(type)) ){
            mStep = Calendar.DAY_OF_WEEK;
            mNumTicks = 1;
        } else if (grossStep > (long) DATE_DAY_TIME){
            mStep = Calendar.HOUR;
            mNumTicks = 6;
        } else if (grossStep > (long) mNumTicks*DATE_HOUR_TIME){
            mStep = Calendar.HOUR;
            mNumTicks = 1;
        } else if (grossStep > DATE_HOUR_TIME){
            mStep = Calendar.MINUTE;
            mNumTicks = 15;
        } else if (grossStep > (long) mNumTicks*DATE_MINUTE_TIME){
            mStep = Calendar.MINUTE;
            mNumTicks = 1;
        } else if (grossStep > DATE_MINUTE_TIME){
            mStep = Calendar.SECOND;
            mNumTicks = 15;
        } else if (DataTypeEnum.DATETIME.getColumnDateType().equals(type)
                    || (grossStep > mNumTicks*DATE_SECOND_TIME && DataTypeEnum.DATETIME64.getColumnDateType().equals(type)) ){
            mStep = Calendar.SECOND;
            mNumTicks = 1;
        } else {
            mStep = Calendar.MILLISECOND;
            mNumTicks = 100;
        }
        return new PivotChartAxisCalibration<>(mMin.getTime(), mMax.getTime(), mStep, mNumTicks);
    }
}
