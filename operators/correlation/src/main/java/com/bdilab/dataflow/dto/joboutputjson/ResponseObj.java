package com.bdilab.dataflow.dto.joboutputjson;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CorrelationResponseObj")
public class ResponseObj {
  private String attribute0;

  private String attribute1;

  private String correlation;
}
