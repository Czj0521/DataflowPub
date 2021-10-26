package com.bdilab.dataflow.common.enums;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/19
 */
public enum PivotChartOperationEnum {
    /**
     * attribute：属性名
     */
    ATTRIBUTE("attribute"),

    /**
     * binning：装箱操作，包括：Equi Wdith Binning 和 Natural Binning
     */
    BINNING("binning"),

    /**
     * aggregation：聚合操作（count（计数）、sum（求和）、distinct count（非重复的属性值数目进行计数）、min（最小值）、max（最大值）、average（平均值））
     */
    AGGREGATION("aggregation"),

    /**
     * sort：排序
     */
    SORT("sort");

    private final String operationName;

    PivotChartOperationEnum(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }
}
