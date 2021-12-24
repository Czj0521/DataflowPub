package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/19 19:57
 * @version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MutualInformationDescription extends JobDescription {
  private String target;
  private String[] features;
}
