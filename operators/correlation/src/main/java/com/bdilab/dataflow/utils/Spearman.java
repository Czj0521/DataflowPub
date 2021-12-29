package com.bdilab.dataflow.utils;

import java.util.*;

public class Spearman {

  public Double scorr(List<Double> x, List<Double> y) {
    if (x.size() != y.size()) {
      throw new IllegalArgumentException("Input vector sizes are different.");
    }
    int n = x.size(); // 获得序列的长度
    Double[] xarr = x.toArray(new Double[n]); // x集合转换为数组
    Collections.sort(x);
    Double[] yarr = y.toArray(new Double[n]); // y集合转换为数组
    Collections.sort(y);

    Map<Double, Integer> xpair = new HashMap<>(); // 统计 x 集合中相同的元素
    Map<Double, Double> xrank = new HashMap<>();  // 存放 x 集合的排行
    Map<Double, Integer> ypair = new HashMap<>(); // 统计 y 集合中相同的元素
    Map<Double, Double> yrank = new HashMap<>();  // 存放 y 集合的排行
    // 统计 x 集合中相同的元素
    for (Double x1 : x) {
      int cont = 1;
      if (xpair.get(x1) != null) {
        cont = xpair.get(x1) + 1;
      }
      xpair.put(x1, cont);
    }
    // 计算 x 集合中各元素的排行
    for (int i = 0; i < n; i++) {
      Double x1 = x.get(i);
      Double rank = i + 1.0;
      if (xrank.get(x1) != null) {
        rank += xrank.get(x1);
      }
      xrank.put(x1, rank);
    }
    // 计算 x 集合中相同元素的平均排行
    for (Double xkey : xpair.keySet()) {
      int nx = xpair.get(xkey);
      if (nx > 1) {
        Double rank = xrank.get(xkey) / nx;
        xrank.put(xkey, rank);
      }
    }
    // 统计 x 集合中各位置的排行
    for (int i = 0; i < n; i++) {
      Double x1 = xarr[i];
      xarr[i] = xrank.get(x1);
    }
    // 统计 y 集合中相同的元素
    for (Double y1 : y) {
      int cont = 1;
      if (ypair.get(y1) != null) {
        cont = ypair.get(y1) + 1;
      }
      ypair.put(y1, cont);
    }
    // 计算 y 集合中各元素的排行
    for (int i = 0; i < n; i++) {
      Double y1 = y.get(i);
      Double rank = i + 1.0;
      if (yrank.get(y1) != null) {
        rank += yrank.get(y1);
      }
      yrank.put(y1, rank);
    }
    // 计算 y 集合中相同元素的平均排行
    for (Double ykey : ypair.keySet()) {
      int ny = ypair.get(ykey);
      if (ny > 1) {
        Double rank = yrank.get(ykey) / ny;
        yrank.put(ykey, rank);
      }
    }
    double avg = 0.0;
    double sum = 0.0;
    // 统计 y 集合中各位置的排行
    for (int i = 0; i < n; i++) {
      Double y1 = yarr[i];
      yarr[i] = yrank.get(y1);
      sum += i + 1.0;
    }
    avg = sum / n;

    double cov = 0.0; // x 和 y 的协方差之积
    double xsd = 0.0; // x 的标准差
    double ysd = 0.0; // y 的标准差
    for (int i = 0; i < n; i++) {
      cov += (xarr[i] - avg) * (yarr[i] - avg);
      xsd += (xarr[i] - avg) * (xarr[i] - avg);
      ysd += (yarr[i] - avg) * (yarr[i] - avg);
    }
    return cov / Math.sqrt(xsd * ysd);
  }


}
