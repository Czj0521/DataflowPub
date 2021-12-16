package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.Data;

@Data
public class WhatIfDescription extends JobDescription {
  private String[] collectors;
  private Object expression;
}
