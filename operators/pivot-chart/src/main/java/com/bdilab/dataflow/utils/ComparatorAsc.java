package com.bdilab.dataflow.utils;

import java.util.Comparator;
import java.util.Date;

/**
 * @author : [zhangpeiliang]
 * @description : [升序比较]
 */
public class ComparatorAsc implements Comparator<Object> {

  @Override
  public int compare(Object o1, Object o2) {
    if (o1 instanceof Double) {
      Double d1 = (Double) o1;
      Double d2 = (Double) o2;
      return d1.compareTo(d2);
    } else if (o1 instanceof String) {
      String s1 = (String) o1;
      String s2 = (String) o2;
      return s1.compareTo(s2);
    } else if (o1 instanceof Date) {
      long t1 = ((Date) o1).getTime();
      long t2 = ((Date) o2).getTime();
      return (int) ((t1 - t2) / 1000);
    } else {
      return o1.hashCode() - o2.hashCode();
    }
  }
}
