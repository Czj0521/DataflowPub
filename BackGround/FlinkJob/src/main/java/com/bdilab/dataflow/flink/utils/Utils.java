package com.bdilab.dataflow.flink.utils;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.typeutils.RowTypeInfo;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/14
 */
public class Utils {
    public static RowTypeInfo getRowTypeInfo(String type){
        RowTypeInfo rowTypeInfo;
        switch (type){
            case "String":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.STRING_TYPE_INFO);
                break;
            case "Boolean":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.BOOLEAN_TYPE_INFO);
                break;
            case "Byte":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.BYTE_TYPE_INFO);
                break;
            case "Short":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.SHORT_TYPE_INFO);
                break;
            case "Integer":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.INT_TYPE_INFO);
                break;
            case "Long":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.LONG_TYPE_INFO);
                break;
            case "Float":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.FLOAT_TYPE_INFO);
                break;
            case "Double":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.DOUBLE_TYPE_INFO);
                break;
            case "Character":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.CHAR_TYPE_INFO);
                break;
            case "Date":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.DATE_TYPE_INFO);
                break;
            case "Void":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.VOID_TYPE_INFO);
                break;
            case "BigInteger":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.BIG_INT_TYPE_INFO);
                break;
            case "BigDecimal":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.BIG_DEC_TYPE_INFO);
                break;
            case "Instant":
                rowTypeInfo = new RowTypeInfo(BasicTypeInfo.INSTANT_TYPE_INFO);
                break;
            default:
                throw new RuntimeException("error column type");
        }
        return rowTypeInfo;
    }
}
