package com.bdilab.dataflow.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ChiSquared test utils.

 * @author YuShaochao
 * @create 2021-12-20
 */
@Component
public class ChiSquaredUtils {

  /**
   * get p value.
   *
   * @return pvalue
   */
  public  double getP(List<Double> datalist1, List<Double> datalist2) {
    //List<Double> datalist1 = Arrays.asList(1374D,500D,72D,1544D,839D,10D,1003D,6D,381D,724D);
    //List<Double> datalist2 = Arrays.asList(736D,229D,48D,841D,456D,3D,617D,5D,211D,401D);
    List<Double> sumdataList = new ArrayList<>();
    double countdata1 = 0, countdata2 = 0, count = 0;
    for (int i = 0; i < datalist1.size(); i++) {
      sumdataList.add(datalist1.get(i) + datalist2.get(i));
      countdata1 += datalist1.get(i);
      countdata2 += datalist2.get(i);
      count += sumdataList.get(i);
    }
    List<Double> predataList1 = new ArrayList<>();
    List<Double> predataList2 = new ArrayList<>();
    for (int i = 0; i < datalist1.size(); i++) {
      predataList1.add(countdata1 / count * sumdataList.get(i));
      predataList2.add(countdata2 / count * sumdataList.get(i));
    }
    //System.out.println(predataList1);
    //System.out.println(predataList2);

    double spuX = 0;
    for (int i = 0; i < datalist1.size(); i++) {
      Double data1 = datalist1.get(i);
      Double predata1 = predataList1.get(i);
      Double data2 = datalist2.get(i);
      Double predata2 = predataList2.get(i);
      spuX += Math.pow(data1 - predata1, 2) / predata1;
      spuX += Math.pow(data2 - predata2, 2) / predata2;
    }
    int dof = datalist1.size() - 1;
    double pValue = chisqr2pValue(dof, spuX);
    return  pValue;
  }

  /**
   * get ChiSquared value x^2.
   *
   * @return ChiSquared value x^2.
   */
  public  double chisqr2pValue(int dof, double chi_squared) {
    if (chi_squared < 0 || dof < 1) {
      return 0.0;
    }
    double k = ((double) dof) * 0.5;
    double v = chi_squared * 0.5;
    if (dof == 2) {
      return Math.exp(-1.0 * v);
    }
    double incompleteGamma = log_igf(k, v);
    // 如果过小或者非数值或者无穷
    if (Math.exp(incompleteGamma) <= 1e-8 || Double.isNaN(Math.exp(incompleteGamma))
        || Double.isInfinite(Math.exp(incompleteGamma))) {
      return 1e-14;
    }
    double gamma = Math.log(getApproxGamma(k));
    incompleteGamma -= gamma;
    if (Math.exp(incompleteGamma) > 1) {
      return 1e-14;
    }
    double pValue = 1.0 - Math.exp(incompleteGamma);
    return (double) pValue;
  }
  /**
   * 求伽马函数的近似公式.

   * @param n
   */
  public  double getApproxGamma(double n) {
    // RECIP_E = (E^-1) = (1.0 / E)
    double RECIP_E = 0.36787944117144232159552377016147;
    // TWOPI = 2.0 * PI
    double TWOPI = 6.283185307179586476925286766559;
    double d = 1.0 / (10.0 * n);
    d = 1.0 / ((12 * n) - d);
    d = (d + n) * RECIP_E;
    d = Math.pow(d, n);
    d *= Math.sqrt(TWOPI / n);
    return d;
  }
  /**
   * 不完全伽马函数.

   * @param s
   * @param z
   */
  public double log_igf(double s, double z) {
    if (z < 0.0) {
      return 0.0;
    }
    double sc = (Math.log(z) * s) - z - Math.log(s);
    double k = KM(s, z);
    return Math.log(k) + sc;
  }

  private static double KM(double s, double z) {
    double sum = 1.0;
    double nom = 1.0;
    double denom = 1.0;
    double log_nom = Math.log(nom);
    double log_denom = Math.log(denom);
    double log_s = Math.log(s);
    double log_z = Math.log(z);
    for (int i = 0; i < 1000; ++i) {
      log_nom += log_z;
      s++;
      log_s = Math.log(s);
      log_denom += log_s;
      double log_sum = log_nom - log_denom;
      sum += Math.exp(log_sum);
    }
    return sum;
  }
}
