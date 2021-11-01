package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.Data;

/**
 * TableDescription.

 * @author: Zunjing Chen
 * @create: 2021-09-18
 */
@Data
public class TableDescription extends JobDescription {
  String filter;
  String[] project;
  String[] group;
}
