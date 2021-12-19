package com.bdilab.dataflow.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.UUID;

/**
 * SQL Parse Utils.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-16
 **/
public class SqlParseUtils {
  /**
   * json String array to list.
   */
  public static List getJsonArrToList(JSONObject json, String filedName, Class clazz) {
    return json.getJSONArray(filedName).toJavaList(clazz);
  }


  /**
   * combine With Separator.

   * @param list String[]
   * @param separator separator
   */
  public static String combineWithSeparator(String[] list, String separator) {
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s);
      sb.append(separator);
    }
    return sb.substring(0, sb.length() - separator.length());
  }

  /**
   * combine With Separator.

   * @param list list
   * @param separator separator
   */
  public static <T> String combineWithSeparator(List<T> list, String separator) {
    StringBuilder sb = new StringBuilder();
    for (T s : list) {
      sb.append(s);
      sb.append(separator);
    }
    return sb.substring(0, sb.length() - separator.length());
  }

  /**
   * Table/view ID.
   */
  public static String getUuid32() {

    return UUID.randomUUID().toString().replace("-", "").toLowerCase();

  }
}
