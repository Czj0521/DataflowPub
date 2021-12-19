package com.bdilab.dataflow.dto.pojo.independvariable;

import com.bdilab.dataflow.utils.SqlParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@Data
@NoArgsConstructor
public class RangeVariable<T> extends BaseVariable {
  private T defaultValue;
  private T lowerBound;
  private T upperBound;
  private T distanceOfValue;

  public RangeVariable(String variableName, T defaultValue, T lowerBound, T upperBound, T distanceOfValue) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.distanceOfValue = distanceOfValue;
  }

  @Override
  public String generateArray() {
    String res = "";
    if (defaultValue instanceof Double) {
      StringBuilder sb = new StringBuilder("[");
      for (Double i = (Double) lowerBound; i < (Double)upperBound; i += (Double)distanceOfValue) {
        sb.append(i).append(",");
      }
      res =  sb.substring(0, sb.length() - 1) + "]";
    } else if (defaultValue instanceof Integer) {
      res = MessageFormat.format("range({0},{1},{2})", lowerBound, upperBound, distanceOfValue);
    }
    return res;
  }
}
