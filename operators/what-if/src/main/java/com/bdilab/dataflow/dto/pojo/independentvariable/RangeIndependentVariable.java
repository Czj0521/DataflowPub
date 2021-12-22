package com.bdilab.dataflow.dto.pojo.independentvariable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Range independent variable for What-If job .

 * @author: wh
 * @create: 2021-12-20
 */
@Data
@NoArgsConstructor
public class RangeIndependentVariable extends BaseIndependentVariable {
  private String defaultValue;
  private String lowerBound;
  private String upperBound;
  private String distanceOfValue;

  /**
   * Constructor.
   */
  public RangeIndependentVariable(String variableName,
                                  String defaultValue,
                                  String lowerBound,
                                  String upperBound,
                                  String distanceOfValue) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.distanceOfValue = distanceOfValue;
  }

  @Override
  public String generateArray() {
    String res = "";
    StringBuilder sb = new StringBuilder("[");
    for (double i = Double.parseDouble(lowerBound);
         i < Double.parseDouble(upperBound);
         i += Double.parseDouble(distanceOfValue)) {
      sb.append(i).append(",");
    }
    res =  sb.substring(0, sb.length() - 1) + "]";
    return res;
  }
}
