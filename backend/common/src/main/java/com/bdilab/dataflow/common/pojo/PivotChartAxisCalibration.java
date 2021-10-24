package com.bdilab.dataflow.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PivotChartAxisCalibration<T> {
    private T min;
    private T max;
    /**
     * numeric类型时 ticks代表坐标轴一个刻度（区间）长度； date类型时 ticks代表一个区间为年或月或日或小时或分钟
     */
    private T step;
    /**
     * numeric类型时 step代表坐标轴刻度（区间）数； date类型时 step代表一个区间的ticks个数
     */
    private Integer ticks;
    private String type;
    private List<T> calibration;

    public PivotChartAxisCalibration(T min, T max, T step, Integer ticks) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.ticks = ticks;
    }
}
