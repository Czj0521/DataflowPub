package com.bdilab.dataflow.utils;

/**
 * 保留三位有效数字.
 *
 * @author Liu Pan
 * @date 2021-12-23
 **/
public class Format {
  private int magnitude(Double price) {
    if (price == 0.0) {
      return 0;
    }
    int fac;
    Long i;
    Long k = 10L;
    if (price > 1) {
      i = price.longValue();
      fac = 0;
      while (i / k != 0L) {
        fac++;
        k *= 10;
      }
    } else {
      fac = -1;
      while (price * k < 1) {
        fac--;
        k *= 10;
      }
    }
    return fac;
  }

  /**
   * 数字格式化 - 最少保留{num}位有效数字 - 保留 {min}~{max} 为小数 - format02.
   */
  public String formatDigit(double price) {
    int min = 2;
    int max = 8;
    int num = 3;
    boolean sign = false;
    Double rate = 1.0;
    Double value = price * rate;
    int level = magnitude(Math.abs(value));
    int count = Math.max(min, Math.min(max, num - level - 1));
    return String.format("%" + (sign ? "+" : "") + "." + count + "f", value);
  }
}
