package com.bdilab.dataflow.dto.jobDescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CorrelationDescription")
public class CorrelationDescription extends JobDescription {
  @ApiModelProperty(value = "目标属性", example = "Age")
  private String target;

  @NotNull
  @ApiModelProperty(value = "特定属性列表", required = true, example = "['Age','Married']")
  private String[] features;

  @NotEmpty
  @ApiModelProperty(value = "相关系数方法", required = true, example = "pearson")
  private String method;

  public CorrelationDescription(String jobType, String dataSource, int limit) {
    super(jobType, new String[]{dataSource}, limit);
  }
}
