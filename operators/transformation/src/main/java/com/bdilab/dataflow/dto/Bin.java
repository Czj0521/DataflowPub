package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Bin dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
@AllArgsConstructor
public class Bin {
  @NotEmpty
  @ApiModelProperty(value = "分箱值", required = true)
  private String binValue;
  @NotEmpty
  @ApiModelProperty(value = "筛选条件（和filter操作符一致）", required = true)
  private String filter;
}
