package com.bdilab.dataflow.dto;

import com.bdilab.dataflow.common.exception.UncheckException;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Independent variable base class.
 *
 * @author Zunjing Chen
 * @date 2021-12-19
 **/
@Data
public class IndependentVariable {

  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private String columnName;
  @NotEmpty
  @ApiModelProperty(value = "默认值", required = true)
  private String defaultValue;
  @NotEmpty
  @ApiModelProperty(value = "数据类型:int/float/string", required = true)
  private String dataType;
  @NotEmpty
  @ApiModelProperty(value = "自变量类型：range/enumeration", required = true)
  private String type;
  @NotEmpty
  @ApiModelProperty(value = "自变量表达式", required = true)
  private String expression;
  @ApiModelProperty(value = "自变量可能的取值", required = true)
  private List<String> possibleValues;
  @ApiModelProperty(value = "最小值", required = true)
  private String lowerBound;
  @ApiModelProperty(value = "最大值", required = true)
  private String upperBound;
  @ApiModelProperty(value = "间隔", required = true)
  private String ofValues;

  public List<String> possibleValues() {
    List<String> result = new ArrayList<>();
    switch (type) {
      case "range":
        switch (dataType) {
          case "int":
            int low = Integer.parseInt(lowerBound);
            int high = Integer.parseInt(upperBound);
            int of = Integer.parseInt(ofValues);
            for (int i = low; i <= high; i = i + of) {
              result.add(String.valueOf(i));
            }
            return result;
          case "float":
            float floatLow = Float.parseFloat(lowerBound);
            float floatHigh = Float.parseFloat(upperBound);
            float floatOf = Float.parseFloat(ofValues);
            for (float i = floatLow; i <= floatHigh; i = i + floatOf) {
              result.add(String.valueOf(i));
            }
            return result;
          default:
            throw new UncheckException("Unknown data type");
        }
      case "enumeration":
        if (possibleValues == null || possibleValues.size() == 0) {
          throw new UncheckException("PossibleValues cant be empty");
        }
        // If variable type is string,all value need to be decorated with '`'.
        if (dataType.equals("string")) {
          for (String v : possibleValues) {
            result.add("'" + v + "'");
          }
          return result;
        }
        return possibleValues;
      default:
        throw new UncheckException("Unknown independent value type");
    }
  }
}
