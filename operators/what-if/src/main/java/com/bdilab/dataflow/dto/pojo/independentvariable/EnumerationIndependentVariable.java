package com.bdilab.dataflow.dto.pojo.independentvariable;

import com.bdilab.dataflow.utils.SqlParseUtils;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Enumeration independent variable for What-If job .

 * @author: wh
 * @create: 2021-12-20
 */
@Data
@NoArgsConstructor
public class EnumerationIndependentVariable extends BaseIndependentVariable {
  private String defaultValue;
  private List<String> possibleValues;

  /**
   * Constructor.
   */
  public EnumerationIndependentVariable(String variableName,
                                        String defaultValue,
                                        List<String> possibleValues) {
    super(variableName);
    this.defaultValue = defaultValue;
    this.possibleValues = possibleValues;
  }

  @Override
  public String generateArray() {
    return "[" + SqlParseUtils.combineWithSeparator(possibleValues, ",") + "]";
  }
}
