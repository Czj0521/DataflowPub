package com.bdilab.dataflow.utils.dag;

import lombok.Data;

@Data
public class OutputDataSlot {
  private String nextNodeId;
  private Integer nextSlotId;

  public OutputDataSlot() {
  }

  public OutputDataSlot(String nextNodeId, Integer nextSlotId) {
    this.nextNodeId = nextNodeId;
    this.nextSlotId = nextSlotId;
  }
}
