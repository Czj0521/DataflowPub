package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TableDescription.
 *
 * @author: Zunjing Chen
 * @create: 2021-09-18
 */
@Data
@NoArgsConstructor
public class TableDescription extends JobDescription {
  private String filter;
  private String[] project;
  private String[] group;

  /**
   * All args constructor.
   *
   */
  public TableDescription(String jobType, String dataSource, Integer limit, String filter,
      String[] project, String[] group) {
    super(jobType, new String[]{dataSource}, limit);
    this.filter = filter;
    this.project = project;
    this.group = group;
  }

  /**
   * Generate TableDescription from json object.
   */
  public static TableDescription generateFromJson(JSONObject json) {
    String filter = json.getString("filter");
    String[] project = json.getJSONArray("project").toArray(new String[]{});
    String[] group = json.getJSONArray("group").toArray(new String[]{});
    String jobType = json.getString("jobType");
    Integer limit = json.getInteger("limit");
    String dataSource = json.getString("dataSource");
    return new TableDescription(jobType, dataSource, limit, filter, project, group);
  }
}
