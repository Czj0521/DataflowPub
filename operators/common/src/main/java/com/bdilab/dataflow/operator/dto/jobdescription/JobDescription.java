package com.bdilab.dataflow.operator.dto.jobdescription;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JobDescription base class.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public abstract class JobDescription {
  @ApiModelProperty(value = "任务类型", required = true, example = "table")
  String jobType;
  @ApiModelProperty(value = "数据源", required = true, example = "data.csv or sql")
  String[] dataSource;
  @ApiModelProperty(value = "返回数据最大数目", required = true, example = "2000")
  Integer limit;
}
