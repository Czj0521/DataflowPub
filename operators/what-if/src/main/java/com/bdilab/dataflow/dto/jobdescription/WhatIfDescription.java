package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIfDescription extends JobDescription {
  private String[] collectors;
}
