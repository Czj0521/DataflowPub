package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.bdilab.dataflow.utils.dag.consts.DagConstants;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

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

  /**
   * Constructor.
   *
   * @param dagNodeInputDto dagNodeInputDto
   */
  public DagNode(DagNodeInputDto dagNodeInputDto) {
    this.nodeId = dagNodeInputDto.getNodeId();
    JSONArray dataSources =
        dagNodeInputDto.getNodeDescription().getJSONArray("dataSource");
    this.inputDataSlots = new InputDataSlot[dataSources.size()];
    for (int i = 0; i < dataSources.size(); i++) {
      this.inputDataSlots[i] = new InputDataSlot(dataSources.getString(i));
    }
    this.outputDataSlots = new ArrayList<>();
    this.nodeType = dagNodeInputDto.getNodeType();
    this.nodeDescription = dagNodeInputDto.getNodeDescription();
  }

  @JSONField(serialize = false)
  public Integer getInputSlotSize() {
    return this.inputDataSlots.length;
  }

  @JSONField(serialize = false)
  public String getPreNodeId(int slotIndex) {
    return this.inputDataSlots[slotIndex].getPreNodeId();
  }

  @JSONField(serialize = false)
  void setPreNodeId(int slotIndex, String preNodeId) {
    this.inputDataSlots[slotIndex].setPreNodeId(preNodeId);
  }

  @JSONField(serialize = false)
  public List<String> getFilterId(int slotIndex) {
    return this.inputDataSlots[slotIndex].getFilterId();
  }

  @JSONField(serialize = false)
  public Map<String, String> getEdgeTypeMap(int slotIndex) {
    return this.inputDataSlots[slotIndex].getEdgeType();
  }

  @JSONField(serialize = false)
  public String getEdgeType(int slotIndex, String preNodeId) {
    return this.getEdgeTypeMap(slotIndex).getOrDefault(preNodeId, DagConstants.DEFAULT_LINE);
  }

  @JSONField(serialize = false)
  public boolean isDefaultEdge(int slotIndex, String preNodeId) {
    return DagConstants.DEFAULT_LINE.equals(getEdgeType(slotIndex, preNodeId));
  }

  @JSONField(serialize = false)
  public boolean isDashedEdge(int slotIndex, String preNodeId) {
    return DagConstants.DASHED_LINE.equals(getEdgeType(slotIndex, preNodeId));
  }

  @JSONField(serialize = false)
  public boolean isBrushEdge(int slotIndex, String preNodeId) {
    return DagConstants.BRUSH_LINE.equals(getEdgeType(slotIndex, preNodeId));
  }

  @JSONField(serialize = false)
  public String getInputDataSource(int slotIndex) {
    return this.inputDataSlots[slotIndex].getDataSource();
  }

  @JSONField(serialize = false)
  void setDataSource(int slotIndex, String dataSource) {
    this.inputDataSlots[slotIndex].setDataSource(dataSource);
    JSONObject nodeDescription = (JSONObject) this.nodeDescription;
    JSONArray decDataSource = nodeDescription.getJSONArray("dataSource");
    decDataSource.set(slotIndex, dataSource);
    nodeDescription.put("dataSource", decDataSource);
  }

  /**
   * Get all input dataSources.
   */
  @JSONField(serialize = false)
  public List<String> getInputDataSources() {
    List<String> dataSources = new ArrayList<>();
    for (int i = 0; i < getInputSlotSize(); i++) {
      dataSources.add(getInputDataSource(i));
    }
    return dataSources;
  }

  @JSONField(serialize = false)
  void removeOutputSlot(OutputDataSlot removeOutputSlot) {
    this.outputDataSlots.remove(removeOutputSlot);
  }
}
