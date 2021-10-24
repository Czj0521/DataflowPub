package com.bdilab.dataflow.common.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/15
 * Table仪表盘的filter操作
 */
public enum DataTypeEnum {
    /**
     * 数据类型转换（ClickHouse-java-数据类型）
     */
    INT8("Int8", "Integer", "numeric"),
    INT16("Int16", "Integer", "numeric"),
    INT32("Int32", "Integer", "numeric"),
    INT64("Int64", "Integer", "numeric"),
    UINT8("UInt8", "Integer", "numeric"),
    UINT16("UInt16", "Integer", "numeric"),
    UINT32("UInt32", "Integer", "numeric"),
    UINT64("UInt64", "Integer", "numeric"),
    FLOAT32("Float32", "Float", "numeric"),
    FLOAT64("Float64", "Double", "numeric"),
    STRING("String", "String", "string"),
    DATE("Date", "Date", "date"),
    DATETIME("DateTime", "DateTime", "date"),
    DATETIME64("DateTime64", "DateTime64", "date");


    private final String clickHouseDateType;
    private final String columnDateType;
    private final String dataType;

    public static final Map<String, String> CLICKHOUSE_COLUMN_DATATYPE_MAP = new HashMap<>();
    public static final Map<String, String> COLUMN_DATATYPE_MAP = new HashMap<>();

    static {
        for (DataTypeEnum dataTypeEnum : EnumSet.allOf(DataTypeEnum.class)){
            CLICKHOUSE_COLUMN_DATATYPE_MAP.put(dataTypeEnum.getClickHouseDateType(), dataTypeEnum.getColumnDateType());
            COLUMN_DATATYPE_MAP.put(dataTypeEnum.getColumnDateType(), dataTypeEnum.getDataType());
        }

    }

    DataTypeEnum(String clickHouseDateType, String columnDateType, String dataType) {
        this.clickHouseDateType = clickHouseDateType;
        this.columnDateType = columnDateType;
        this.dataType = dataType;
    }

    public String getClickHouseDateType() {
        return clickHouseDateType;
    }

    public String getColumnDateType() {
        return columnDateType;
    }

    public String getDataType() {
        return dataType;
    }
}