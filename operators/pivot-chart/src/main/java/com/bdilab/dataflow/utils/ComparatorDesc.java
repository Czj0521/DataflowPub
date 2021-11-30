package com.bdilab.dataflow.utils;

import java.util.Comparator;
import java.util.Date;

/**
 * @author : [zhangpeiliang]
 * @description : [降序比较]
 */
public class ComparatorDesc implements Comparator<Object> {

  @Override
  public int compare(Object o1, Object o2) {
    if (o1 instanceof Double) {
      Double d1 = (Double) o1;
      Double d2 = (Double) o2;
      return d2.compareTo(d1);
    } else if (o1 instanceof String) {
      String s1 = (String) o1;
      String s2 = (String) o2;
      return s2.compareTo(s1);
    } else if (o1 instanceof Date) {
      long t1 = ((Date) o1).getTime();
      long t2 = ((Date) o2).getTime();
      return (int) ((t2 - t1) / 1000);
    } else {
      return o2.hashCode() - o1.hashCode();
    }
  }
}
