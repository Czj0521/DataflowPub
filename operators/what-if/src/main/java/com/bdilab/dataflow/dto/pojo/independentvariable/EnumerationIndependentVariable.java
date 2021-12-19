package com.bdilab.dataflow.dto.pojo.independentvariable;

import com.bdilab.dataflow.utils.SqlParseUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EnumerationIndependentVariable<T> extends BaseIndependentVariable {
  private T defaultValue;
  private List<T> possibleValues;

  public EnumerationIndependentVariable(String variableName, T defaultValue, List<T> possibleValues) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.possibleValues = possibleValues;
  }

  @Override
  public String generateArray() {
    return "[" + SqlParseUtils.combineWithSeparator(possibleValues, ",") + "]";
  }
}
