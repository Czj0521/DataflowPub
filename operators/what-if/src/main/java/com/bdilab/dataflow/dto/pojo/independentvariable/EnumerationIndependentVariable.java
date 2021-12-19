package com.bdilab.dataflow.dto.pojo.independentvariable;

import com.bdilab.dataflow.utils.SqlParseUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EnumerationIndependentVariable extends BaseIndependentVariable {
  private String defaultValue;
  private List<String> possibleValues;

  public EnumerationIndependentVariable(String variableName, String defaultValue, List<String> possibleValues) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.possibleValues = possibleValues;
  }

  @Override
  public String generateArray() {
    return "[" + SqlParseUtils.combineWithSeparator(possibleValues, ",") + "]";
  }
}
