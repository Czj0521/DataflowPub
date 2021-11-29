package com.bdilab.dataflow.utils.dag;

import java.util.ArrayList;
import java.util.List;
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
   * For fastjson serialize.
   */
  public InputDataSlot() {
  }

  public InputDataSlot(String dataSource) {
    this.dataSource = dataSource;
  }
}
