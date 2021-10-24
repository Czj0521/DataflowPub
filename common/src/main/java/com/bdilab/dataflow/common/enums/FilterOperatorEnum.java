package com.bdilab.dataflow.common.enums;

import com.bdilab.dataflow.common.consts.OperatorConstants;

import java.util.NoSuchElementException;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-13
 * @description:
 **/
public enum FilterOperatorEnum {
    // string
    STRING_EQUALS("string", "equals", OperatorConstants.COLUMN_MAGIC_NUMBER + " = '"+OperatorConstants.VALUE_MAGIC_NUMBER+"'",true),
    STRING_CONTAINS("string","contains","",false),
    STRING_CONTAINS_ANY("string","contains any","",false),
    STRING_CONTAINS_ALL("string","contains all","",false),
    STRING_STARTS_WITH("string","starts with","startsWith("+OperatorConstants.COLUMN_MAGIC_NUMBER+",'"+OperatorConstants.VALUE_MAGIC_NUMBER+"')",true),
    STRING_ENDS_WITH("string","ends with","endsWith("+OperatorConstants.COLUMN_MAGIC_NUMBER+",'"+OperatorConstants.VALUE_MAGIC_NUMBER+"')",true),
    STRING_NOT_CONTAINS("string","does not contains","",false),
    // numeric
    NUMERIC_EQUALS("numeric","equals(==)",OperatorConstants.COLUMN_MAGIC_NUMBER +"=" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    NUMERIC_NOT_EQUALS("numeric","does not equals(!=)",OperatorConstants.COLUMN_MAGIC_NUMBER +"!=" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    NUMERIC_GREATER_THAN("numeric","greater than(>)",OperatorConstants.COLUMN_MAGIC_NUMBER +">" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    NUMERIC_GREATER_THAN_OR_EQUAL_TO("numeric","greater than or equal to(>=)",OperatorConstants.COLUMN_MAGIC_NUMBER +">=" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    NUMERIC_LESS_THAN("numeric","less than(<)",OperatorConstants.COLUMN_MAGIC_NUMBER +"<" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    NUMERIC_LESS_THAN_OR_EQUAL_TO("numeric","less than or equal to(<=)",OperatorConstants.COLUMN_MAGIC_NUMBER +"<=" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    // date
    DATE_IS("date","is",OperatorConstants.COLUMN_MAGIC_NUMBER +"=" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    DATE_BEFORE("date","before",OperatorConstants.COLUMN_MAGIC_NUMBER +"<" +OperatorConstants.VALUE_MAGIC_NUMBER,true),
    DATE_AFTER("date","after",OperatorConstants.COLUMN_MAGIC_NUMBER +">" +OperatorConstants.VALUE_MAGIC_NUMBER,true);
    // boolean
//    BOOLEAN_IS("boolean","is",)
    /**
     * 数据类型 string numeric date boolean
     */
    String dataType;
    /**
     * 前端操作名称
     */
    String filterOperatorName;
    /**
     * 后端sql函数
     */
    String SQLParam;
    /**
     * click是否原生支持
     */
    boolean support;
    public static FilterOperatorEnum getFilterOperator(String operatorName) {
        for (FilterOperatorEnum filterOperatorEnum : FilterOperatorEnum.values()) {
            if (filterOperatorEnum.filterOperatorName.equals(operatorName)) {
                return filterOperatorEnum;
            }
        }
        throw new NoSuchElementException();
    }
    FilterOperatorEnum(String dataType, String filterOperatorName, String SQLParam,boolean support) {
        this.dataType = dataType;
        this.filterOperatorName = filterOperatorName;
        this.SQLParam = SQLParam;
        this.support = support;
    }

    public String getFilterOperatorName() {
        return filterOperatorName;
    }

    public String getSQLParam() {
        return SQLParam;
    }

    public String getDataType() {
        return dataType;
    }
}
