package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * transpose's dto.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TransposeDescription")
public class TransposeDescription extends JobDescription {
  @NotEmpty
  @ApiModelProperty(value = "横轴", required = true, example = "name")
  private String column;
  @NotNull
  @ApiModelProperty(value = "横轴数据类型", required = true, example = "false/ture")
  private boolean columnIsNumeric;
  @NotNull
  @ApiModelProperty(value = "group列名列表", required = true, example = "['name','age']")
  private String[] groupBy;
  @NotNull
  @ApiModelProperty(value = "计算属性名称和对应聚合计算方式{'name':'sum',\n 'age':'average'}", required = true)
  private Map<String, String> attributeWithAggregationMap;
  @NotNull
  @ApiModelProperty(value = "横轴值最大数量", required = true, example = "20（横轴最多20个值）")
  private int topTransposedValuesNum;

  public TransposeDescription(String jobType, String dataSource,
                              int limit, int topTransposedValuesNum) {
    super(jobType, dataSource, limit);
    this.topTransposedValuesNum = topTransposedValuesNum;
  }

  public TransposeDescription(String jobType, String dataSource, int limit) {
    super(jobType, dataSource, limit);
    topTransposedValuesNum = 20;
  }
}
