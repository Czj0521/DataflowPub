package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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


  public Boolean hasTarget() {
    return !(target == null || target.isEmpty());
  }

  public Boolean hasAggregation() {
    return !(aggregation == null || aggregation.isEmpty());
  }

  public Boolean hasDataSource() {
    return !(getDataSource() == null || getDataSource().length < 1 || getDataSource()[0].isEmpty());
  }

  public Boolean valid() {
    return hasTarget() && hasAggregation() && hasDataSource();
  }
}
