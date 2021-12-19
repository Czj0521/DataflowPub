package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Binarizer dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class Binarizer {

  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private String newColumnName;

  @NotEmpty
  @ApiModelProperty(value = "新列名", required = true)
  private String filter;

  @Override
  public String toString() {
    return "if(" + filter + ",1,0) AS " + newColumnName;

  }
}
