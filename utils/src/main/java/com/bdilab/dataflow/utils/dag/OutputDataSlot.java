package com.bdilab.dataflow.utils.dag;

import lombok.Data;

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
