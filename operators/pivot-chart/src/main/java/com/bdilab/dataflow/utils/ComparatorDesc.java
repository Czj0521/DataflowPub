package com.bdilab.dataflow.utils;

import java.util.Comparator;

/**
 * @author : [zhangpeiliang]
 * @description : [降序比较]
 */
public class ComparatorDesc implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    return o2.compareTo(o1);
  }
}
