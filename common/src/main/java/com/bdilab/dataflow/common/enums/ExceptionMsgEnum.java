package com.bdilab.dataflow.common.enums;

/**
 * Exception Msg Enum.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-17
 **/
public enum ExceptionMsgEnum {
  TABLE_METADATA_ERROR("Table 元数据有误"),
  CLICKHOUSE_HTTP_ERROR("ClickHouse Http 请求有误"),
  TABLE_SQL_PARSE_ERROR("TABLE_SQL_PARSE_ERROR"),
  TRANSFORMATION_ERROR("Transformation 表达式有误");
  private String msg;

  ExceptionMsgEnum(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
