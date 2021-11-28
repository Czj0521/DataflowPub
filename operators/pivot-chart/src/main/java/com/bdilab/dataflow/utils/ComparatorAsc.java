package com.bdilab.dataflow.utils;

import java.util.Comparator;

/**
 * @author : [zhangpeiliang]
 * @description : [升序比较]
 */
public class ComparatorAsc implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }
}
