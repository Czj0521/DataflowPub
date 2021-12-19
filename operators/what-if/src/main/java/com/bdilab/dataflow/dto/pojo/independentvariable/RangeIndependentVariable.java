package com.bdilab.dataflow.dto.pojo.independentvariable;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@Data
@NoArgsConstructor
public class RangeIndependentVariable extends BaseIndependentVariable {
  private String defaultValue;
  private String lowerBound;
  private String upperBound;
  private String distanceOfValue;

  public RangeIndependentVariable(String variableName, String defaultValue, String lowerBound, String upperBound, String distanceOfValue) {
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
    for (Double i = Double.valueOf(lowerBound); i < Double.valueOf(upperBound); i += Double.valueOf(distanceOfValue)) {
      sb.append(i).append(",");
    }
    res =  sb.substring(0, sb.length() - 1) + "]";
    return res;
  }
}
