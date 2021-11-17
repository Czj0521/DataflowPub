package com.bdilab.dataflow.dto;


import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Profiler operator description.

 * @author YuShaochao
 * @create 2021-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerDescription {
  private String jobType;
  private String dataSource;
  private List<String> profilerColumnList;


  /**
   * Generator TransposeDescription from json.
   */
  public static ProfilerDescription generateFromJson(JSONObject json) {
    return new ProfilerDescription(
        json.getString("jobType"),
        json.getString("dataSource"),
        (List<String>) json.get("profilerColumnList")
    );
  }
}
