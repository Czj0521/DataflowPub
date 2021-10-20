package com.bdilab.dataflow.common.enums;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-17
 * @description:
 **/
public enum ExceptionMsgEnum {
    TABLE_METADATA_ERROR("Table 元数据有误"),
    CLICKHOUSE_HTTP_ERROR("ClickHouse Http 请求有误"),
    TABLE_SQL_PARSE_ERROR("TABLE_SQL_PARSE_ERROR");
    private String msg;

    ExceptionMsgEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
