package com.bdilab.dataflow.dto.pojo.independvariable;

import com.bdilab.dataflow.utils.SqlParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EnumerationVariable<T> extends BaseVariable {
  private T defaultValue;
  private List<T> possibleValues;

  public EnumerationVariable(String variableName, T defaultValue, List<T> possibleValues) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.possibleValues = possibleValues;
  }

  @Override
  public String generateArray() {
    return "[" + SqlParseUtils.combineWithSeparator(possibleValues, ",") + "]";
  }
}
