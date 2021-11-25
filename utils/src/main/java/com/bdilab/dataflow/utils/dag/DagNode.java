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

//  /**
//   * Constructor.
//   *
//   * @param nodeId node ID
//   * @param nodeType node type
//   */
//  public DagNode(String nodeId, String nodeType, Object nodeDescription, Integer slotSize) {
//    this.nodeId = nodeId;
//    this.nodeDataSlots = new NodeDataSlot[slotSize];
//    this.nodeType = nodeType;
//    this.nodeDescription = nodeDescription;
//  }

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
}
