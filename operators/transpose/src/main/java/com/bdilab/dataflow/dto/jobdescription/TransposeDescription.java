package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * transpose dto.
 *
 * @author Zunjing Chen
 * @date 2021-09-23
 */
@Data
@NoArgsConstructor
@ApiModel(value = "TransposeDescription")
public class TransposeDescription extends JobDescription {

  @NotEmpty
  @ApiModelProperty(value = "横轴", required = true, example = "name")
  private String column;
  @NotNull
  @ApiModelProperty(value = "横轴数据类型", required = true, example = "false/ture")
  private boolean columnIsNumeric;
  @NotNull
  @ApiModelProperty(value = "group列名列表", required = true, example = "['name','age']")
  private String[] groupBy;
  @NotNull
  @ApiModelProperty(value = "计算属性名称和对应聚合计算方式{'name':'sum',\n 'age':'average'}", required = true)
  private Map<String, String> attributeWithAggregationMap;
  @NotNull
  @ApiModelProperty(value = "横轴值最大数量", required = true, example = "20（横轴最多20个值）")
  private int topTransposedValuesNum;

  /**
   * All args constructor.
   */
  public TransposeDescription(String jobType, String dataSource,
      int limit, String column, boolean columnIsNumeric, String[] groupBy,
      Map<String, String> attributeWithAggregationMap, int topTransposedValuesNum) {
    super(jobType, new String[]{dataSource}, limit);
    this.column = column;
    this.columnIsNumeric = columnIsNumeric;
    this.attributeWithAggregationMap = attributeWithAggregationMap;
    this.groupBy = groupBy;
    this.topTransposedValuesNum = topTransposedValuesNum;
  }

  public TransposeDescription(String jobType, String dataSource, int limit) {
    super(jobType, new String[]{dataSource}, limit);
    topTransposedValuesNum = 20;
  }

  /**
   * Generator TransposeDescription from json.
   */
  public static TransposeDescription generateFromJson(JSONObject json) {
    String column = json.getString("column");
    Boolean columnIsNumeric = json.getBoolean("columnIsNumeric");
    String[] groupBy = json.getJSONArray("groupBy").toArray(new String[]{});
    Map<String, String> attributeWithAggregationMap = json
        .getObject("attributeWithAggregationMap", new TypeReference<Map<String, String>>() {
        });
    Integer topTransposedValuesNum =
        json.containsKey("topTransposedValuesNum") ? json.getInteger("topTransposedValuesNum") : 20;
    String jobType = json.getString("jobType");
    Integer limit = json.getInteger("limit");
    String dataSource = json.getString("datasource");
    return new TransposeDescription(jobType, dataSource, limit, column, columnIsNumeric, groupBy,
        attributeWithAggregationMap, topTransposedValuesNum);
  }
}
