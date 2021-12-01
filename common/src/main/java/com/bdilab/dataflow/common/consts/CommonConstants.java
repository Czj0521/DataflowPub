package com.bdilab.dataflow.common.consts;

/**
 * Common Constants.

 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
public class CommonConstants {
  /**
   * The date format.
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATETIME64_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  /**
   * The data type.
   */
  public static final String NUMERIC_NAME = "numeric";
  public static final String STRING_NAME = "string";
  public static final String DATE_NAME = "date";

  /**
   * Clickhouse database.
   */
  public static final String DATABASE = "dataflow";

  /**
   * Clickhouse table prefix。
   */
  public static final String TEMP_TABLE_PREFIX = "temp_";
  public static final String TEMP_INPUT_TABLE_PREFIX = "temp_input_";
  public static final String CPL_TEMP_TABLE_PREFIX = CommonConstants.DATABASE + "." + CommonConstants.TEMP_TABLE_PREFIX;
  public static final String CPL_TEMP_INPUT_TABLE_PREFIX = CommonConstants.DATABASE + "." + CommonConstants.TEMP_INPUT_TABLE_PREFIX;

  public static final String MATERIALIZE_PREFIX = "materialize_";
  public static final String CPL_MATERIALIZE_PREFIX = CommonConstants.DATABASE + "." + CommonConstants.MATERIALIZE_PREFIX;
}
