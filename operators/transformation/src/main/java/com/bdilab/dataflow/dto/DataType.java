package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DataType dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class DataType {

  @NotEmpty
  @ApiModelProperty(value = "转换列名", required = true)
  private String columnName;
  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private String newColumnName;
  @NotEmpty
  @ApiModelProperty(value = "转换后的数据类型", required = true)
  private String dataType;

  @Override
  public String toString() {
    String type = "";
    switch (dataType) {
      case "integer":
        type = "Int32";
        break;
      case "boolean":
        type = "Int8";
        break;
      case "float":
        type = "Float32";
        break;
      case "string":
        type = "String";
        break;
      case "double":
        type = "Float64";
      default:
        break;
    }
    return "to" + type + "(" + columnName + ") AS " + newColumnName;
  }
}
