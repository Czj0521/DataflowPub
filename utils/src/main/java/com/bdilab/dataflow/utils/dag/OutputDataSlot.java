package com.bdilab.dataflow.utils.dag;

import lombok.Data;

/**
 * The output data-slot of node.
 *
 * @author wh
 * @date 2021/11/16
 */
@Data
public class OutputDataSlot {
  private String nextNodeId;
  private Integer nextSlotIndex;

  public OutputDataSlot() {
  }

  public OutputDataSlot(String nextNodeId, Integer nextSlotIndex) {
    this.nextNodeId = nextNodeId;
    this.nextSlotIndex = nextSlotIndex;
  }
}
