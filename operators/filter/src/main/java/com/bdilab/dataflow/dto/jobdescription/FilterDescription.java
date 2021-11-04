package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter job description.

 * @author: wh
 * @create: 2021-10-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDescription extends JobDescription {
  String filter;

  public FilterDescription(String jobType, String dataSource, Integer limit, String filter) {
    super(jobType, dataSource, limit);
    this.filter = filter;
  }

  /**
   * Generator TransposeDescription from json.
   */
  public static FilterDescription generateFromJson(JSONObject json) {
    return new FilterDescription(
        json.getString("jobType"),
        json.getString("dataSource"),
        json.getInteger("limit"),
        json.getString("filter")
        );
  }
}
