package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * FindReplace dto.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class FindReplace {

  @NotEmpty
  @ApiModelProperty(name = "替换列名", required = true)
  private String searchInColumnName;
  @ApiModelProperty(name = "新列名", required = true)
  private String newColumnName;
  @ApiModelProperty(name = "需要替换字符串", required = true)
  private String search;
  @ApiModelProperty(name = "替换后的字符串", required = true)
  private String replaceWith;

  @Override
  public String toString() {
    return "replaceAll(" + searchInColumnName + ",'" + search + "','"
        + replaceWith + "') AS " + newColumnName;
  }
}
