package com.bdilab.dataflow.utils.dag;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
