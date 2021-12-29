package com.bdilab.dataflow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 1维k-means创建自然分箱.
 * @ author: [zhangpeiliang]
 */
public class Kmeans {

  /**
   * 聚类函数主体.
   * 针对一维 double 数组，指定聚类数目 k.
   * 将数据聚成 k 类.
   */
  public static double[][] cluster(Double[] p, int k) {
    // 存放聚类旧的聚类中心
    double[] oldCenter = new double[k];
    // 存放新计算的聚类中心
    double[] newCenter = new double[k];
    // 存放放回结果
    double[][] g = new double[k][];
    // 初始化聚类中心
    // 经典方法是随机选取 k 个
    // 本例中先将输入数据均分k份，然后每隔k份取一个值，这样更快收敛
    // 聚类中心的选取不影响最终结果
    for (int i = 0; i < k; i++) {
      int i1 = p.length / k;
      oldCenter[i] = p[i1 * i];
    }
    // 循环聚类，更新聚类中心
    // 到聚类中心不变为止或者最大循环50次后，直接终止循环
    int flag = 0;
    while (flag < 50) {
      // 根据聚类中心将元素分组
      g = group(p, oldCenter);
      // 计算分组后的聚类中心
      for (int i = 0; i < g.length; i++) {
        newCenter[i] = center(g[i]);
      }
      flag++;
      // 如果聚类中心不同
      if (!equal(newCenter, oldCenter)) {
        // 为下一次聚类准备
        oldCenter = newCenter;
        newCenter = new double[k];
      } else {
        // 聚类结束
        break;
      }
    }
    // 返回聚类结果
    return g;
  }

  /**
   * 聚类中心函数.
   * 简单的一维聚类返回其算数平均值.
   * 可扩展.
   */
  public static double center(double[] p) {
    return sum(p) / p.length;
  }

  /**
   * 给定 double 型数组 p 和聚类中心 c.
   * 根据 c 将 p 中元素聚类。返回二维数组.
   * 存放各组元素.
   */
  public static double[][] group(Double[] p, double[] c) {
    // 中间变量，用来分组标记
    int[] gi = new int[p.length];
    // 考察每一个元素 pi 同聚类中心 cj 的距离
    // pi 与 cj 的距离最小则归为 j 类
    for (int i = 0; i < p.length; i++) {
      // 存放距离
      double[] d = new double[c.length];
      // 计算到每个聚类中心的距离
      for (int j = 0; j < c.length; j++) {
        d[j] = distance(p[i], c[j]);
      }
      // 找出最小距离，返回最小值的下标
      int ci = min(d);
      // 标记属于哪一组
      gi[i] = ci;
    }
    // 存放分组结果
    double[][] g = new double[c.length][];
    // 遍历每个聚类中心，分组
    for (int i = 0; i < c.length; i++) {
      // 中间变量，记录聚类后每一组的大小
      int s = 0;
      // 计算每一组的长度
      for (int k : gi) {
        if (k == i) {
          s++;
        }
      }
      // 存储每一组的成员
      g[i] = new double[s];
      s = 0;
      // 根据分组标记将各元素归位
      for (int j = 0; j < gi.length; j++) {
        if (gi[j] == i) {
          g[i][s] = p[j];
          s++;
        }
      }
    }
    // 返回分组结果
    return g;
  }

  /**
   * 计算两个点之间的距离， 这里采用最简单得一维欧氏距离， 可扩展.
   */
  public static double distance(double x, double y) {
    return Math.abs(x - y);
  }

  /**
   * 返回给定 double 数组各元素之和.
   */
  public static double sum(double[] p) {
    double sum = 0.0;
    for (double v : p) {
      sum += v;
    }
    return sum;
  }

  /**
   * 给定 double 类型数组，返回最小值的下标.
   */
  public static int min(double[] p) {
    int i = 0;
    double m = p[0];
    for (int j = 1; j < p.length; j++) {
      if (p[j] < m) {
        i = j;
        m = p[j];
      }
    }
    return i;
  }

  /**
   * 判断两个 double 数组是否相等。 长度一样且对应位置值相同返回真.
   */
  public static boolean equal(double[] a, double[] b) {
    if (a.length != b.length) {
      return false;
    } else {
      for (int i = 0; i < a.length; i++) {
        if (a[i] != b[i]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 返回自然分箱的箱子集合.
   */
  public static Set<Object> naturalBinning(Double[] p, boolean includeZero) {
    List<Number> list = new ArrayList<>();
    //默认15个聚类
    int k = 15;
    //总数据小于15，则聚类数为数据的总数
    if (p.length < k) {
      k = p.length;
    }
    double[][] g = cluster(p, k);
    if (includeZero) {
      //包含0,往箱子集合加0
      list.add(0.0);
    }
    for (double[] doubles : g) {
      list.add(doubles[0]);
    }
    list.add(g[k - 1][g[k - 1].length - 1] + 1);
    return new TreeSet<>(list);
  }
}