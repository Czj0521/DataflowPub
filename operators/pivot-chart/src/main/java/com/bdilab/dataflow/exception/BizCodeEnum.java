package com.bdilab.dataflow.exception;

/**
 * 异常枚举.
 * @ author: [zhangpeiliang]
 */
public enum BizCodeEnum {
  MENU_NUM_EXCEPTION(1001, "菜单数目异常，必须包含所有的6个菜单"),
  MENU_REPEAT_EXCEPTION(1002, "菜单重复，必须包含所有6个不同的菜单"),
  MENU_BOTH_BIN_AGR_EXCEPTION(1003, "菜单同时包含聚合和分箱属性，只能保留其中之一"),
  MENU_NONE_ATTRIBUTE_EXCEPTION(1004, "菜单属性为none，聚合、分箱、排序也必须为none"),

  INVALID_AGGREGATION_TYPE(2001, "非法聚合类型，请检查"),
  INVALID_BINNING_TYPE(2002, "非法分箱类型，请检查"),
  INVALID_SORT_TYPE(2003, "非法排序类型，请检查"),
  INVALID_TYPE(2004, "非法类型，请检查"),

  CANNOT_FIND_COLUMN(500, "找不到选择的列"),

  UNKNOWN_EXCEPTION(999, "未知异常，请联系管理员");

  private final int code;

  private final String msg;

  BizCodeEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
