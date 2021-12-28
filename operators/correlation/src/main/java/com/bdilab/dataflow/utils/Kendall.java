package com.bdilab.dataflow.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * kendall 相关系数.
 *
 * @author: Liu pan
 * @create: 2021-12-15
 * @description:
 */
public class Kendall {
  private long count;

  /**
   * kendall 相关系数.
   *
   * @param x target集合.
   * @param y features集合.
   * @return x 与 y 的kendall相关性度量
   */
  public Double kcorr(List<Double> x, List<Double> y) {
    if (x.size() != y.size()) {
      throw new IllegalArgumentException("Input vector sizes are different.");
    }

    double n4 = 0; // 合并序列xy中相同元素总的组合对数
    Map<Double, Integer> xpair = new HashMap<>(); // 集合x中由相同元素组成的各个子集的元素数
    Map<Double, Integer> ypair = new HashMap<>(); // 集合y中由相同元素组成的各个子集的元素数
    Map<String, Integer> xypair = new HashMap<>(); // 合并序列xy中由相同元素组成的各个子集的元素数
    //计算xpair中各个元素的值
    for (Double x1 : x) {
      int cont = 1;
      if (xpair.get(x1) != null) {
        cont = xpair.get(x1) + 1;
      }
      xpair.put(x1, cont);
    }
    double n1 = 0; // 集合x中相同元素总的组合对数
    // 计算 n1 的值
    for (Double xkey : xpair.keySet()) {
      int nx = xpair.get(xkey);
      if (nx > 1) {
        n1 += 0.5 * nx * (nx - 1);
      }
    }
    // 计算ypair中各个元素的值
    for (Double y1 : y) {
      int cont = 1;
      if (ypair.get(y1) != null) {
        cont = ypair.get(y1) + 1;
      }
      ypair.put(y1, cont);
    }
    double n2 = 0; // 集合y中相同元素总的组合对数
    // 计算 n2 的值
    for (Double ykey : ypair.keySet()) {
      int ny = ypair.get(ykey);
      if (ny > 1) {
        n2 += 0.5 * ny * (ny - 1);
      }
    }
    int n = x.size(); // 得到序列的长度
    double n3 = 0; // 合并序列xy的总对数
    // 计算 n3 的值
    n3 = 0.5 * n * (n - 1);
    // 计算xypair中各个元素的值
    for (int i = 0; i < n; i++) {
      int cont = 1;
      String xy1 = x.get(i) + "," + y.get(i);
      if (xypair.get(xy1) != null) {
        cont = xypair.get(xy1) + 1;
      }
      xypair.put(xy1, cont);
    }
    // 计算 n4 的值
    for (String xykey : xypair.keySet()) {
      int nxy = xypair.get(xykey);
      if (nxy > 1) {
        n4 += 0.5 * nxy * (nxy - 1);
      }
    }

    Double[] xarr = x.toArray(new Double[n]);
    Double[] xtemp = x.toArray(new Double[n]);
    Double[] yarr = y.toArray(new Double[n]);
    Double[] ytemp = y.toArray(new Double[n]);
    initialMergeSort(xarr, yarr, 0, n - 1, xtemp, ytemp);
    long s = inversePairs(yarr);
    double numerator = n3 - n1 - n2 + n4 - 2 * s;
    double n0 = (n3 - n1) * (n3 - n2);
    double denominator = Math.sqrt(n0);
    return numerator / denominator;
  }

  /**
   * 将合并序列 xy 按照 x 进行初始排序.
   */
  private void initialMerge(Double[] xarr, Double[] yarr, int low, int mid, int high, Double[] xtmp, Double[] ytmp) {
    int xi = 0;
    int yi = 0;
    int xj = low;
    int yj = low;
    int xk = mid + 1;  //左边序列和右边序列起始索引
    int yk = mid + 1;  //左边序列和右边序列起始索引
    while (xj <= mid && xk <= high) {
      if (xarr[xj] < xarr[xk] || (xarr[xj].equals(xarr[xk]) && yarr[xj] <= yarr[xk])) {
        xtmp[xi++] = xarr[xj++];
        ytmp[yi++] = yarr[yj++];
      } else {
        xtmp[xi++] = xarr[xk++];
        ytmp[yi++] = yarr[yk++];
      }
    }
    //若左边序列还有剩余，则将其全部拷贝进tmp[]中
    while (xj <= mid) {
      xtmp[xi++] = xarr[xj++];
      ytmp[yi++] = yarr[yj++];
    }

    while (xk <= high) {
      xtmp[xi++] = xarr[xk++];
      ytmp[yi++] = yarr[yk++];
    }

    for (int t = 0; t < xi; t++) {
      xarr[low + t] = xtmp[t];
      yarr[low + t] = ytmp[t];
    }
  }

  /**
   * 将合并序列 xy 按照 x 进行初始排序.
   */
  private void initialMergeSort(Double[] xarr, Double[] yarr, int low, int high, Double[] xtmp, Double[] ytmp) {
    if (low < high) {
      int mid = (low + high) / 2;
      initialMergeSort(xarr, yarr, low, mid, xtmp, ytmp); //对左边序列进行归并排序
      initialMergeSort(xarr, yarr, mid + 1, high, xtmp, ytmp);  //对右边序列进行归并排序
      initialMerge(xarr, yarr, low, mid, high, xtmp, ytmp);    //合并两个有序序列
    }
  }

  /**
   * 运用二次归并计算冒泡排序需要交换的次数.
   */
  private long inversePairs(Double[] array) {
    if (array == null || array.length == 0) {
      return 0L;
    }
    count = 0;
    mergeSort(array, 0, array.length - 1);
    return count;
  }

  private void mergeSort(Double[] input, int left, int right) {
    int mid = (left + right) / 2;
    if (left < right) {
      // 左边
      mergeSort(input, left, mid);
      // 右边
      mergeSort(input, mid + 1, right);
      // 左右归并
      sort(input, left, mid, right);
    }
  }

  private void sort(Double[] input, int left, int center, int right) {
    Double []tempArray = new Double[right - left + 1];
    int mid = center + 1;
    int current = 0;
    int temp = left;
    while (left <= center && mid <= right) {
      if (input[left] > input[mid]) {
        tempArray[current++] = input[mid++];
        count += center - left + 1;
      } else {
        tempArray[current++] = input[left++];
      }
    }
    //只会执行一个
    while (left <= center) {
      tempArray[current++] = input[left++];
    }
    while (mid <= right) {
      tempArray[current++] = input[mid++];
    }
    current = 0;
    while (temp <= right) {
      input[temp++] = tempArray[current++];
    }
  }
}
