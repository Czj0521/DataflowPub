package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What-If job description.

 * @author: wh
 * @create: 2021-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatIfDescription extends JobDescription {
  private String[] collectors;
}
