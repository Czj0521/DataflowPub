package com.bdilab.dataflow.dto.joboutputjson;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : [zhangpeiliang]
 * @description : [响应对象]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespObj {
  /**
   * 菜单名.
   */
  private String menu;

  /**
   * 属性名，有单纯的属性，聚合属性，分箱属性.
   */
  private String name;

  /**
   * 值的基数集合，用于坐标轴或展示基准.
   */
  private Set<Object> distinctValues;

  /**
   * 属性的所有值.
   */
  private List<?> values;

  /**
   * 返回数据的数量.
   */
  private int size;
}
