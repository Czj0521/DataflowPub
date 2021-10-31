package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
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
public class TransposeDescription extends JobDescription {
  @NotEmpty
  String column;
  @NotNull
  boolean columnIsNumeric;
  @NotNull
  String[] groupBy;
  @NotNull
  Map<String, String> attributeWithAggregationMap;

  public TransposeDescription(String jobType, String dataSource, Integer limit) {
    super(jobType, dataSource, limit);
  }
}
