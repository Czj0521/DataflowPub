package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * CustomBinning dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class CustomBinning {

  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private String newColumnName;
  @NotEmpty
  @ApiModelProperty(value = "默认分箱值", required = true)
  private String defaultBin;
  @NotEmpty
  @ApiModelProperty(value = "该列是否为数子", required = true)
  // base on datatype to generate sql
  private Boolean isNumeric;
  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private List<Bin> bins;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("multiIf(");
    for (Bin bin : bins) {
      if (!isNumeric) {
        bin.setBinValue("'" + bin.getBinValue() + "'");
      }
      stringBuilder.append(bin.getFilter()).append(",").append(bin.getBinValue()).append(",");
    }
    if (!isNumeric) {
      defaultBin = "'" + defaultBin + "'";
    }
    stringBuilder.append(defaultBin).append(") AS ").append(newColumnName);
    return stringBuilder.toString();
  }
}