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
}
