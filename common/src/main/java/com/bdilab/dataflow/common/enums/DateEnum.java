package com.bdilab.dataflow.common.enums;
/**
 * @author wh
 * @version 1.0
 * @date 2021/09/18
 */
public enum DateEnum {
    /**
     * 用于日期类型区分
     */
    YEAR("year",1),
    MONTH("month",2),
    WEEK("week",3),
    DAY("day",4),
    HOUR("hour",5),
    MINUTE("minute",6),
    SECOND("second",7);

    private final String DateFieldNmae;
    private final Integer index;

    DateEnum(String dateFieldNmae, Integer index) {
        DateFieldNmae = dateFieldNmae;
        this.index = index;
    }

    public String getDateFieldNmae() {
        return DateFieldNmae;
    }

    public Integer getIndex() {
        return index;
    }
}
