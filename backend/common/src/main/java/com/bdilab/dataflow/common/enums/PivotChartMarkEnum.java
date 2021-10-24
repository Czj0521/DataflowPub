package com.bdilab.dataflow.common.enums;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 * PivotChart仪表盘类型 枚举类
 */
public enum PivotChartMarkEnum {
    /**
     * 图表类型 ——条形图，折线图，面积图，散点图，点线图
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
