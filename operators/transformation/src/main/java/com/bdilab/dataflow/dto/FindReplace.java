package com.bdilab.dataflow.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
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
  @ApiModelProperty(name = "需要替换字符串-替换后的字符串", required = true)
  private Map<String, String> searchReplaceMap;
//  @ApiModelProperty(name = "需要替换字符串", required = true)
//  private String search;
//  @ApiModelProperty(name = "替换后的字符串", required = true)
//  private String replaceWith;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("multiIf(");

    for (Map.Entry<String, String> entry : searchReplaceMap.entrySet()) {
      String search = entry.getKey();
      String replaceWith = entry.getValue();
      stringBuilder.append(searchInColumnName).append("='").append(search).append("','").append(replaceWith).append("',");
    }
    stringBuilder.append(searchInColumnName).append(") AS ").append(newColumnName);
    return stringBuilder.toString();
  }
}
