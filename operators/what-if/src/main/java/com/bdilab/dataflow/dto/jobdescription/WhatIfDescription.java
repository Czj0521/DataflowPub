package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.dto.pojo.independvariable.BaseVariable;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIfDescription extends JobDescription {
  private String[] collectors;
}
