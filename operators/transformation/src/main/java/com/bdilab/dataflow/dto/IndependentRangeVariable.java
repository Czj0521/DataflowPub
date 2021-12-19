package com.bdilab.dataflow.dto;

import com.bdilab.dataflow.common.exception.UncheckException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * IndependentVariable,type is range(numeric).
 *
 * @author Zunjing Chen
 * @date 2021-12-19
 **/
@Data
public class IndependentRangeVariable extends IndependentVariableBase {

  private String lowerBound;
  private String upperBound;
  private String ofValues;

  @Override
  public List<String> possibleValues() {
    List<String> result = new ArrayList<>();
    switch (type) {
      case "int":
        int low = Integer.parseInt(lowerBound);
        int high = Integer.parseInt(upperBound);
        int of = Integer.parseInt(ofValues);
        for (int i = low; i <= high; i = i + of) {
          result.add(String.valueOf(i));
        }
        return result;
      case "float":
        float floatLow = Integer.parseInt(lowerBound);
        float floatHigh = Integer.parseInt(upperBound);
        float floatOf = Integer.parseInt(ofValues);
        for (float i = floatLow; i <= floatHigh; i = i + floatOf) {
          result.add(String.valueOf(i));
        }
        return result;
      default:
        throw new UncheckException("Unknown data type");
    }
  }
}
