package com.bdilab.dataflow.utils.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The input data-slot of node.
 *
 * @author wh
 * @date 2021/11/16
 */
@Data
public class InputDataSlot {
  private String dataSource;
  private String preNodeId;
  private List<String> filterId = new ArrayList<>();

  /**
   * 0 : default line. (They are not stored in map.)
   * 1 : dashed line.
   * 2 : brush line.
   */
  private Map<String, String> edgeType = new HashMap<>();

  /**
   * For fastjson serialize.
   */
  public InputDataSlot() {
  }

  public InputDataSlot(String dataSource) {
    this.dataSource = dataSource;
  }
}
