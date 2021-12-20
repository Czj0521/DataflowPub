package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : [zhangpeiliang]
 * @description : [透视图描述对象]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel
public class PivotChartDescription extends JobDescription {

  /**
   * 选择了属性或其他菜单选项的菜单集合.
   */
  private Menu[] menus;

  /**
   * 过滤字段，联动场景使用.
   */
  private String filter;

  /**
   * 是否只更新filter字段，由前端在联动时给出，默认情况下为false.
   */
  private Boolean onlyUpdateFilter = false;
}
