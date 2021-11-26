package com.bdilab.dataflow.utils.dag;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * The node of dag.
 *
 * @author wh
 * @date 2021/11/12
 */
@Data
public class DagNode {
  /**
   * Unique identifier that cannot be repeated.
   */
  private String nodeId;

  private InputDataSlot[] inputDataSlots;

  /**
   * The list containing the ID of the subsequent nodes.
   */
  private List<OutputDataSlot> outputDataSlots;

  /**
   * The type of operator.
   */
  private String nodeType;

  /**
   * The description of the node job.
   */
  private Object nodeDescription;

  /**
   * For fastjson serialize.
   */
  private DagNode() {
  }

  public DagNode(DagNodeInputDto dagNodeInputDto){
    this.nodeId = dagNodeInputDto.getNodeId();
    JSONArray dataSources = ((JSONObject) dagNodeInputDto.getNodeDescription()).getJSONArray("dataSource");
    this.inputDataSlots = new InputDataSlot[dataSources.size()];
    for (int i = 0; i < dataSources.size(); i++) {
      this.inputDataSlots[i] = new InputDataSlot(dataSources.getString(i));
    }
    this.outputDataSlots = new ArrayList<>();
    this.nodeType = dagNodeInputDto.getNodeType();
    this.nodeDescription = dagNodeInputDto.getNodeDescription();
  }

  public Integer getInputSlotSize() {
    return this.inputDataSlots.length;
  }

  public String getPreNodeId(int slotIndex) {
    return this.inputDataSlots[slotIndex].getPreNodeId();
  }

  void setPreNodeId(int slotIndex, String preNodeId) {
    this.inputDataSlots[slotIndex].setPreNodeId(preNodeId);
  }

  public List<String> getFilterId(int slotIndex) {
    return this.inputDataSlots[slotIndex].getFilterId();
  }

  public String getDataSource(int slotIndex) {
    return this.inputDataSlots[slotIndex].getDataSource();
  }

  void setDataSource(int slotIndex, String dataSource) {
    this.inputDataSlots[slotIndex].setDataSource(dataSource);
  }

  public List<String> getAllDataSources() {
    List<String> dataSources = new ArrayList<>();
    for (InputDataSlot inputDataSlot : this.inputDataSlots) {
      dataSources.add(inputDataSlot.getDataSource());
    }
    return dataSources;
  }
}
