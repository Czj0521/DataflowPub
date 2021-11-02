package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.common.enums.DataTypeEnum;
import java.util.HashSet;
import java.util.Set;

/**
 * Gets some kind of ClickHouse data type.

 * @author:zhb
 * @create:2021.9.26
 */
public class DataTypeUtils {
  /**
   * 获取数值类型.
   */
  public static Set<String> getNumber() {
    Set<String> set = new HashSet<>();

    set.add(DataTypeEnum.INT8.getClickHouseDateType());
    set.add(DataTypeEnum.INT16.getClickHouseDateType());
    set.add(DataTypeEnum.INT32.getClickHouseDateType());
    set.add(DataTypeEnum.INT64.getClickHouseDateType());
    set.add(DataTypeEnum.UINT8.getClickHouseDateType());
    set.add(DataTypeEnum.UINT16.getClickHouseDateType());
    set.add(DataTypeEnum.UINT32.getClickHouseDateType());
    set.add(DataTypeEnum.UINT64.getClickHouseDateType());
    set.add(DataTypeEnum.FLOAT32.getClickHouseDateType());
    set.add(DataTypeEnum.FLOAT64.getClickHouseDateType());

    return set;
  }
}
