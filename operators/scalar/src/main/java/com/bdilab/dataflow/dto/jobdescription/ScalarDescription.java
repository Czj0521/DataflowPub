package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/13 15:43
 * @version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScalarDescription extends JobDescription {
  private String target;
  private String aggregation;
}
