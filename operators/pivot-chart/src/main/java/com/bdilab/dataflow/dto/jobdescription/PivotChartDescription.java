package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  /**
   * 对前端输入的菜单进行处理，得到最终的输入菜单集合.
   */
  public List<Menu> getInputMenus() {

    Map<String, Menu> map = new HashMap<>();
    for (Menu menu : menus) {
      if (!menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
        if (menu.getMenu().equalsIgnoreCase("column")) {
          Menu rowMenu = map.get(menu.getAttributeRenaming());
          if (!(rowMenu!= null && rowMenu.getMenu().equalsIgnoreCase("row"))) {
            map.put(menu.getAttributeRenaming(), menu);
          }
        } else {
          map.put(menu.getAttributeRenaming(), menu);
        }
      }
    }

    List<Menu> inputMenus = new ArrayList<>();
    for (Map.Entry<String, Menu> entry : map.entrySet()) {
      inputMenus.add(entry.getValue());
    }
    return inputMenus;
  }

  /**
   * 用于返回给前端的菜单选项信息.
   */
  public Map<String, Object> getInfoMap() {
    Map<String, Object> infoMap = new HashMap<>();
    for (Menu menu : menus) {
      infoMap.put(menu.getMenu(), menu.getAttributeRenaming());
    }
    return infoMap;
  }

  /**
   * 在x菜单或y菜单中，只有一个菜单选择了属性+count/属性+distinct count，且另外一个菜单没有选择属性+聚合，展示数据会显示百分比.
   * 该方法就是提取这个菜单重命名，用于百分比计算.
   */
  public String getColumnForPercentage() {
    int flag = 0;
    String column = null;
    for (Menu menu : menus) {
      if (menu.getMenu().equalsIgnoreCase("row") ||
              menu.getMenu().equalsIgnoreCase("column")) {
        if (!menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
          return null;
        }
      }

      if (menu.getMenu().equalsIgnoreCase("x-axis") ||
              menu.getMenu().equalsIgnoreCase("y-axis")) {
        if (!menu.getAggregation().equalsIgnoreCase(Communal.NONE)) {
          flag++;
          if (menu.getAggregation().equalsIgnoreCase(AggregationConstants.COUNT) ||
                  menu.getAggregation().equalsIgnoreCase(AggregationConstants.DISTINCT_COUNT)) {
            column = menu.getAttributeRenaming();
          }
        }
      }
    }

    if (flag != 1) {
      column = null;
    }
    return column;
  }

}