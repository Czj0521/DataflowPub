package com.bdilab.dataflow.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * IndependentVariable,type is enumeration.
 *
 * @author Zunjing Chen
 * @date 2021-12-19
 **/
@Data
public class IndependentEnumerationVariable extends IndependentVariableBase {

  /**
   * Independent variable possible values.  If variable type is string,all value need to be
   * decorated with '`'.
   */
  @Override
  public List<String> possibleValues() {
    if (super.getType().equals("string")) {
      List<String> result = new ArrayList<>();
      for (String v : possibleValues) {
        result.add("'" + v + "'");
      }
      return result;
    }
    return possibleValues;

  }
}
