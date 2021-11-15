package com.bdilab.dataflow.utils.dag;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

  /**
   * The list containing the ID of the preceding nodes.
   */
  private List<String> preNodesId;

  /**
   * The list containing the ID of the subsequent nodes.
   */
  private List<String> nextNodesId;

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

  public DagNode(String nodeId) {
    if (nodeId == null || "".equals(nodeId)) {
      throw new RuntimeException("NodeId can not be empty !");
    }
    this.nodeId = nodeId;
    this.preNodesId = new ArrayList<>();
    this.nextNodesId = new ArrayList<>();
  }

  public DagNode(String nodeId, String nodeType) {
    if (nodeId == null || "".equals(nodeId)) {
      throw new RuntimeException("NodeId can not be empty !");
    }
    this.nodeId = nodeId;
    this.preNodesId = new ArrayList<>();
    this.nextNodesId = new ArrayList<>();
    this.nodeType = nodeType;
  }

  public DagNode(String nodeId, String nodeType, Object nodeDescription) {
    if (nodeId == null || "".equals(nodeId)) {
      throw new RuntimeException("NodeId can not be empty !");
    }
    this.nodeId = nodeId;
    this.preNodesId = new ArrayList<>();
    this.nextNodesId = new ArrayList<>();
    this.nodeType = nodeType;
    this.nodeDescription = nodeDescription;
  }
}
